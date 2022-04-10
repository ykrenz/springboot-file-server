package com.ykrenz.file.service;

import com.ykrenz.file.config.StorageProperties;
import com.ykrenz.file.dao.FileDao;
import com.ykrenz.file.exception.ApiException;
import com.ykrenz.file.model.ApiErrorMessage;
import com.ykrenz.file.model.CommonUtils;
import com.ykrenz.file.dao.FileModel;
import com.ykrenz.file.model.result.InitUploadMultipartResult;
import com.ykrenz.file.model.request.*;
import com.ykrenz.file.model.result.FileResult;
import com.ykrenz.file.model.result.ListMultipartResult;
import com.ykrenz.file.upload.storage.FileServerClient;
import com.ykrenz.file.upload.storage.StorageType;
import com.ykrenz.file.upload.storage.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ykren
 * @date 2022/3/4
 */
@Service
public class FileServiceImpl implements FileService {

    private final long multipartMinSize;
    private final long multipartMaxSize;
    private final int expireDay;
    private final FileServerClient fileServerClient;
    private final FileDao fileDao;

    public FileServiceImpl(StorageProperties storageProperties,
                           Map<StorageType, FileServerClient> fileServerMap,
                           FileDao fileDao) {

        this.fileServerClient = fileServerMap.get(storageProperties.getStorage());
        this.fileDao = fileDao;
        this.multipartMinSize = storageProperties.getMultipartMinSize().toBytes();
        this.multipartMaxSize = storageProperties.getMultipartMaxSize().toBytes();
        this.expireDay = storageProperties.getExpireDay();
    }

    @Override
    public FileResult upload(SimpleUploadRequest request) throws IOException {
        UploadResponse response = uploadServer(request);
        String fileId = saveFile(response);
        FileResult fileResult = convert2FileResult(response);
        fileResult.setId(fileId);
        return fileResult;
    }

    @Override
    public FileResult fastUpload(FastUploadRequest request) {
        FileModel fileModel = fileDao.getOneByHash(request.getHash(),request.getSliceMd5());
        if (fileModel == null) {
            throw new ApiException(ApiErrorMessage.FILE_NOT_FOUND);
        }
        fileModel.setFileName(request.getFileName());
        String fileId = fileDao.save(fileModel);
        FileResult fileResult = convert2FileResult(fileModel);
        fileResult.setId(fileId);
        return fileResult;
    }

    private UploadResponse uploadServer(SimpleUploadRequest request) throws IOException {
        UploadRequest uploadRequest = new UploadRequest();
        MultipartFile file = request.getFile();
        uploadRequest.setFile(file);
        return fileServerClient.upload(uploadRequest);
    }

    @Override
    public InitUploadMultipartResult initMultipart(InitUploadMultipartRequest request) {
        checkInitMultipartParam(request);

        InitRequest initRequest = new InitRequest();
        initRequest.setFileName(request.getFileName());
        initRequest.setFileSize(request.getFileSize());
        initRequest.setPartSize(request.getPartSize());
        InitResponse response = fileServerClient.initMultipart(initRequest);

        InitUploadMultipartResult initUploadMultipartResult = new InitUploadMultipartResult();
        initUploadMultipartResult.setUploadId(response.getUploadId());
        initUploadMultipartResult.setHashMode(fileServerClient.hash().getValue());
        initUploadMultipartResult.setExpireTime(-1);
        if (expireDay > 0) {
            long createTime = response.getCreateTime();
            initUploadMultipartResult.setExpireTime(CommonUtils.plusDays(createTime, expireDay));
        }
        return initUploadMultipartResult;
    }

    @Override
    public ListMultipartResult listMultipart(ListMultipartRequest request) {
        MultiPartUploadResponse multiPartUploadResponse = fileServerClient.uploadParts(request.getUploadId());

        ListMultipartResult result = new ListMultipartResult();
        result.setUploadId(multiPartUploadResponse.getUploadId());
        result.setFileSize(multiPartUploadResponse.getFileSize());
        result.setPartSize(multiPartUploadResponse.getPartSize());

        List<PartResponse> parts = multiPartUploadResponse.getParts();
        result.setParts(CollectionUtils.isEmpty(parts) ?
                Collections.emptyList() : parts.stream()
                .map(PartResponse::getPartNumber)
                .sorted().collect(Collectors.toList()));
        return null;
    }

    private void checkInitMultipartParam(InitUploadMultipartRequest request) {
        Long partSize = request.getPartSize();
        if (partSize < multipartMinSize || partSize > multipartMaxSize) {
            String msg = String.format("分片大小必须在%dM~%dM之间", multipartMinSize / 1024 / 1024, multipartMaxSize / 1024 / 1024);
            throw new ApiException(msg);
        }
    }


    @Override
    public void uploadMultipart(UploadMultipartRequest request) throws IOException {
        MultipartRequest multipartRequest = new MultipartRequest();
        multipartRequest.setUploadId(request.getUploadId());
        multipartRequest.setPartFile(request.getFile());
        multipartRequest.setPartNumber(request.getPartNumber());
        fileServerClient.uploadMultipart(multipartRequest);
    }

    @Override
    public FileResult completeMultipart(CompleteMultipartRequest request) {
        CompleteRequest completeRequest = new CompleteRequest();
        completeRequest.setUploadId(request.getUploadId());
        completeRequest.setHash(request.getHash());
        UploadResponse response = fileServerClient.completeMultipart(completeRequest);
        String fileId = saveFile(response);
        FileResult fileResult = convert2FileResult(response);
        fileResult.setId(fileId);
        return fileResult;
    }

    @Override
    public void cancelMultipart(CancelMultipartRequest request) {
        fileServerClient.abortMultipart(request.getUploadId());
    }

    @Override
    public FileResult info(FileInfoRequest request) {
        String fileId = request.getId();
        FileModel fileModel = fileDao.getById(fileId);
        if (fileModel == null) {
            throw new ApiException(ApiErrorMessage.FILE_NOT_FOUND);
        }
        FileResult fileResult = convert2FileResult(fileModel);
        fileResult.setId(fileId);
        return fileResult;
    }

    @Override
    public void deleteAllFiles() {
//        fileServerClient.deleteAllFiles();
    }


    private String saveFile(UploadResponse response) {
        return fileDao.save(FileModel.builder()
                .fileName(response.getFileName())
                .fileSize(response.getFileSize())
                .bucketName(response.getBucketName())
                .objectName(response.getObjectName())
                .hash(response.getHash())
                .build());
    }


    private FileResult convert2FileResult(UploadResponse response) {
        FileResult fileResult = new FileResult();
        fileResult.setFileName(response.getFileName());
        fileResult.setFileSize(response.getFileSize());
        fileResult.setBucketName(response.getBucketName());
        fileResult.setObjectName(response.getObjectName());
        fileResult.setHash(response.getHash());
        //            fileResult.setUrl(fileServerClient2.getUrl());
        return fileResult;
    }

    private FileResult convert2FileResult(FileModel fileModel) {
        FileResult fileResult = new FileResult();
        fileResult.setFileName(fileModel.getFileName());
        fileResult.setFileSize(fileModel.getFileSize());
        fileResult.setBucketName(fileModel.getBucketName());
        fileResult.setObjectName(fileModel.getObjectName());
        //            fileResult.setUrl(fileServerClient2.getUrl());
        return fileResult;
    }

}
