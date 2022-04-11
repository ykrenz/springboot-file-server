package com.ykrenz.file.upload.storage;

import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.common.Crc32;
import com.ykrenz.fastdfs.model.UploadMultipartPartRequest;
import com.ykrenz.fastdfs.model.fdfs.FileInfo;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import com.ykrenz.file.exception.BizException;
import com.ykrenz.file.model.BizErrorMessage;
import com.ykrenz.file.model.CommonUtils;
import com.ykrenz.file.upload.storage.model.*;
import com.ykrenz.file.upload.manager.*;
import com.ykrenz.file.upload.manager.UploadModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FdfsServerClient implements FileServerClient {

    private final FastDfs fastDfs;

    private final UploadManager<InitUploadModel, UploadModel, PartModel> uploadManager;

    public FdfsServerClient(FastDfs fastDfs, UploadManager<InitUploadModel, UploadModel, PartModel> uploadManager) {
        this.fastDfs = fastDfs;
        this.uploadManager = uploadManager;
    }

    @Override
    public StorageType type() {
        return StorageType.fastdfs;
    }

    @Override
    public HashType hash() {
        return HashType.CRC32;
    }

    @Override
    public UploadResponse upload(UploadRequest request) throws IOException {
        MultipartFile file = request.getFile();
        StorePath storePath = fastDfs.uploadFile(file.getInputStream(), file.getSize(), getFileExtension(file));
        UploadResponse response = new UploadResponse();
        response.setFileName(file.getOriginalFilename());
        response.setFileSize(file.getSize());
        response.setBucketName(storePath.getGroup());
        response.setObjectName(storePath.getPath());
        response.setHash(getHash(storePath));
        return response;
    }

    private String getHash(StorePath storePath) {
        FileInfo fileInfo = fastDfs.queryFileInfo(storePath.getGroup(), storePath.getPath());
        if (fileInfo == null) {
            return null;
        }
        return String.valueOf(fileInfo.getCrc32());
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return getFileExtension(originalFilename);
    }

    private String getFileExtension(String fileName) {
        return CommonUtils.getFileExtension(fileName);
    }

    @Override
    public InitResponse initMultipart(InitRequest request) {
        long fileSize = request.getFileSize();
        String fileExtension = getFileExtension(request.getFileName());
        StorePath storePath = fastDfs.initMultipartUpload(fileSize, fileExtension);
        // 生成uploadId 保存初始化信息
        InitUploadModel initUploadModel = createInitUpload(request, storePath);
        UploadModel uploadModel = uploadManager.createUpload(initUploadModel);
        InitResponse response = new InitResponse();
        response.setUploadId(uploadModel.getUploadId());
        response.setCreateTime(uploadModel.getCreateTime());
        return response;
    }

    private InitUploadModel createInitUpload(InitRequest request, StorePath storePath) {
        InitUploadModel initUploadModel = new InitUploadModel();
        initUploadModel.setFileName(request.getFileName());
        initUploadModel.setFileSize(request.getFileSize());
        initUploadModel.setBucketName(storePath.getGroup());
        initUploadModel.setObjectName(storePath.getPath());
        initUploadModel.setPartSize(request.getPartSize());
        return initUploadModel;
    }

    @Override
    public void uploadMultipart(MultipartRequest request) throws IOException {
        String uploadId = request.getUploadId();
        MultipartFile partFile = request.getPartFile();
        Integer partNumber = request.getPartNumber();
        InitUploadModel upload = this.checkUpload(uploadId);

        String bucketName = upload.getBucketName();
        String objectName = upload.getObjectName();
        long size = partFile.getSize();
        UploadMultipartPartRequest uploadMultipartPartRequest = UploadMultipartPartRequest.builder()
                .groupName(bucketName)
                .path(objectName)
                .streamPart(partFile.getInputStream(), size, partNumber, upload.getPartSize())
                .build();
        fastDfs.uploadMultipart(uploadMultipartPartRequest);

        PartModel partModel = new PartModel();
        partModel.setUploadId(uploadId);
        partModel.setSize(size);
        partModel.setBucketName(bucketName);
        partModel.setObjectName(objectName);
        partModel.setPartNumber(partNumber);
        uploadManager.savePart(partModel);
    }


    @Override
    public UploadResponse completeMultipart(CompleteRequest request) {
        String uploadId = request.getUploadId();
        InitUploadModel upload = this.checkUpload(uploadId);
        StorePath storePath = fastDfs.completeMultipartUpload(upload.getBucketName(), upload.getObjectName());

        if (!checkCrc32(request.getHash(), storePath)) {
            abortMultipart(uploadId);
            throw new BizException(BizErrorMessage.CHECK_HASH_ERROR, true);
        }
        UploadResponse response = new UploadResponse();
        response.setFileName(upload.getFileName());
        response.setFileSize(upload.getFileSize());
        response.setBucketName(storePath.getGroup());
        response.setObjectName(storePath.getPath());
        response.setHash(request.getHash());

        uploadManager.clearParts(uploadId);
        return response;
    }

    private boolean checkCrc32(String crc32, StorePath path) {
        if (StringUtils.isBlank(crc32)) {
            return false;
        }
        FileInfo fileInfo = fastDfs.queryFileInfo(path.getGroup(), path.getPath());
        long crcUnsigned = Crc32.convertUnsigned(fileInfo.getCrc32());
        return StringUtils.equalsIgnoreCase(crc32, String.valueOf(crcUnsigned));
    }

    @Override
    public void abortMultipart(String uploadId) {
        InitUploadModel upload = this.checkUpload(uploadId);
        fastDfs.deleteFile(upload.getBucketName(), upload.getObjectName());
        uploadManager.clearParts(uploadId);
    }

    @Override
    public MultiPartUploadResponse uploadParts(String uploadId) {
        InitUploadModel upload = this.checkUpload(uploadId);
        List<PartModel> partModels = uploadManager.listParts(uploadId);
        MultiPartUploadResponse multiPartUploadResponse = new MultiPartUploadResponse();
        multiPartUploadResponse.setUploadId(uploadId);
        multiPartUploadResponse.setFileSize(upload.getFileSize());
        multiPartUploadResponse.setPartSize(upload.getPartSize());
        List<PartResponse> parts = partModels.stream().map(o -> {
            PartResponse part = new PartResponse();
            part.setPartNumber(o.getPartNumber());
            part.setSize(o.getSize());
            part.setETag(o.getETag());
            return part;
        }).collect(Collectors.toList());
        multiPartUploadResponse.setParts(parts);
        return multiPartUploadResponse;
    }

    @Override
    public List<UploadTask> listUpload() {
        List<UploadModel> uploadModels = uploadManager.listUploads(new ListUploadParam());
        return uploadModels.stream().map(o -> {
            UploadTask task = new UploadTask();
            task.setUploadId(o.getUploadId());
            task.setCreateTime(o.getCreateTime());
            return task;
        }).collect(Collectors.toList());
    }

    @Override
    public InputStream downLoadInputStream(DownLoadRequest request) {
        long[] range = request.getRange();
        if (range != null) {
            return fastDfs.downloadFile(request.getBucketName(), request.getObjectName(), range[0], range[1], ins -> ins);
        }
        return fastDfs.downloadFile(request.getBucketName(), request.getObjectName(), ins -> ins);
    }

    private InitUploadModel checkUpload(String uploadId) {
        InitUploadModel upload = uploadManager.getUpload(uploadId);
        if (Objects.isNull(upload)) {
            throw new BizException(BizErrorMessage.UPLOAD_ID_NOT_FOUND, true);
        }
        return upload;
    }

}
