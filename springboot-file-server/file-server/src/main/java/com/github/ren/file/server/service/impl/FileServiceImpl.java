package com.github.ren.file.server.service.impl;

import com.github.ren.file.client.objectname.UuidGenerator;
import com.github.ren.file.client.starter.StorageType;
import com.github.ren.file.server.client.*;
import com.github.ren.file.server.config.FileUploadProperties;
import com.github.ren.file.server.entity.TFile;
import com.github.ren.file.server.entity.TUpload;
import com.github.ren.file.server.ex.ApiException;
import com.github.ren.file.server.model.ErrorCode;
import com.github.ren.file.server.model.request.*;
import com.github.ren.file.server.model.result.CheckResult;
import com.github.ren.file.server.model.result.InitPartResult;
import com.github.ren.file.server.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 文件接口逻辑类
 * @Author ren
 * @Since 1.0
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private MultipartUploadAdapter multipartUploadAdapter;

    @Autowired
    private TFileServiceImpl tFileService;

    @Autowired
    private TUploadServiceImpl tUploadService;

    private long maxUploadSize = 1024 * 1024 * 10;

    private long multipartMinSize = 1024 * 100;

    private long multipartMaxSize = 1024 * 1024 * 100;

    private final StorageType storage;

    private final int storageTypeInt;

    public FileServiceImpl(FileUploadProperties fileUploadProperties) {
        long maxUploadSize = fileUploadProperties.getMaxUploadSize().toBytes();
        if (maxUploadSize != 0) {
            this.maxUploadSize = multipartMaxSize;
        }

        long multipartMaxSize = fileUploadProperties.getMultipartMaxSize().toBytes();
        if (multipartMaxSize > 1) {
            this.multipartMaxSize = multipartMaxSize;
        }

        long multipartMinSize = fileUploadProperties.getMultipartMinSize().toBytes();
        if (multipartMinSize != 0) {
            this.multipartMinSize = multipartMinSize;
        }

        this.storage = fileUploadProperties.getStorage();
        this.storageTypeInt = fileUploadProperties.getStorageTypeInt();
    }

    public String objectName(String filename) {
        return new UuidGenerator(filename).generator();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(SimpleUploadRequest request) {
//        MultipartFile file = request.getFile();
//        if (file.getSize() > maxUploadSize) {
//            throw new ApiException(ErrorCode.FILE_TO_LARGE);
//        }
//        try {
//            String objectName = objectName(file.getOriginalFilename());
//            UploadGenericResult result = fileClient.upload(file.getInputStream(), objectName);
//            objectName = result.getObjectName();
//            return objectName;
//        } catch (IOException e) {
//            log.error("上传失败", e);
//            throw new ApiException(ErrorCode.UPLOAD_ERROR);
//        }
        return null;
    }

    private TUpload checkUploadId(String uploadId) {
        TUpload tUpload = tUploadService.selectUpload(uploadId);
        if (tUpload == null || tUpload.getStatus() != 0) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        LocalDateTime expireAt = tUpload.getExpireAt();
        if (expireAt != null && LocalDateTime.now().isAfter(expireAt)) {
            throw new ApiException(ErrorCode.UPLOAD_ID_EXPIRE);
        }
        return tUpload;
    }

    private int getMaxPartCount(long filesize, long partSize) {
        return Math.max(1, (int) Math.ceil(filesize / (float) partSize));
    }

    private TUpload checkMultipart(UploadPartRequest uploadPartRequest) {
        MultipartFile file = uploadPartRequest.getFile();
        String uploadId = uploadPartRequest.getUploadId();
        TUpload tUpload = checkUploadId(uploadId);
        Integer partNumber = uploadPartRequest.getPartNumber();
        long currentSize = file.getSize();

        //判断分片大小
        if (currentSize > multipartMaxSize) {
            throw new ApiException(ErrorCode.FILE_PART_SIZE_ERROR, "必须小于" + multipartMaxSize / 1024 / 1024 + "M");
        }
        Long partSize = tUpload.getPartSize();
        Long fileSize = tUpload.getFileSize();

        int maxPartCount = getMaxPartCount(fileSize, partSize);

        boolean lastPart = false;

        //最后一个分片 如果实际分片size>partSize 则partNumber = maxPartCount-1
        if (currentSize > partSize) {

            if (partNumber != maxPartCount - 1) {
                throw new ApiException(ErrorCode.FILE_PART_COUNT_ERROR);
            }

            if (fileSize != partSize * (partNumber - 1) + currentSize) {
                throw new ApiException(ErrorCode.FILE_PART_TOTAL_SIZE_ERROR);
            }

            lastPart = true;
        }
        //最后一个分片 如果实际分片size<partSize 则partNumber = maxPartCount
        if (currentSize < partSize) {
            if (partNumber != maxPartCount) {
                throw new ApiException(ErrorCode.FILE_PART_COUNT_ERROR);
            }

            lastPart = true;
        }

        //文件大小是分片大小的整数倍
        if (currentSize == partSize && partNumber == maxPartCount) {
            lastPart = true;
        }

        if (lastPart) {
//            throw new ApiException(ErrorCode.FILE_PART_COUNT_ERROR);
        }
        return tUpload;
    }

    private List<PartResult> getMultiParts(TUpload tUpload) {
        String objectName = tUpload.getObjectName();
        List<PartResult> partResults = tUploadService.listMultipart(tUpload);
        if (partResults.isEmpty()) {
            ListPartsArgs listPartsArgs = ListPartsArgs.builder()
                    .uploadId(tUpload.getUploadId())
                    .objectName(objectName)
                    .build();
            partResults = multipartUploadAdapter.listParts(storage, listPartsArgs);
        }
        return partResults;
    }

    @Override
    public CheckResult check(CheckRequest request) {
        CheckResult checkResult = new CheckResult();
//        String md5 = request.getMd5();
//        if (StringUtils.isNotBlank(md5)) {
//        checkResult.setExist(false);
//        }
        String uploadId = request.getUploadId();
        TUpload tUpload = checkUploadId(uploadId);
        List<PartResult> multiParts = getMultiParts(tUpload);
        List<com.github.ren.file.server.model.result.PartResult> partResults = new ArrayList<>(multiParts.size());
        for (PartResult multiPart : multiParts) {
            com.github.ren.file.server.model.result.PartResult partResult = new com.github.ren.file.server.model.result.PartResult();
            partResult.setPartNumber(multiPart.getPartNumber());
            partResult.setPartSize(multiPart.getPartSize());
            partResult.setETag(multiPart.getETag());
            partResults.add(partResult);
        }
        checkResult.setParts(partResults);
        return checkResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InitPartResult initMultipart(InitPartRequest request) {
        String filename = request.getFilename();
        Long filesize = request.getFilesize();
        Long partsize = request.getPartsize();
        if (partsize > multipartMaxSize) {
            throw new ApiException(ErrorCode.FILE_PART_SIZE_ERROR, "必须小于" + multipartMaxSize / 1024 / 1024 + "M");
        }

        String objectName = this.objectName(FilenameUtils.getExtension(filename));
        InitMultipartUploadArgs initMultipartUploadArgs = InitMultipartUploadArgs.builder()
                .objectName(objectName)
                .fileSize(filesize)
                .partSize(partsize)
                .build();
        InitMultipartResponse initMultipartResponse = multipartUploadAdapter.initMultipartUpload(storage, initMultipartUploadArgs);

        TUpload tUpload = new TUpload();
        tUpload.setUploadId(initMultipartResponse.getUploadId());
        tUpload.setObjectName(initMultipartResponse.getObjectName());
        tUpload.setCreateTime(LocalDateTime.now());

//        tUpload.setExpireAt(expire);
        tUpload.setStorage(storageTypeInt);
        tUpload.setFileSize(filesize);
        tUpload.setPartSize(partsize);
        tUpload.setStatus(0);
        tUploadService.save(tUpload);
        InitPartResult initPartResult = new InitPartResult();
        initPartResult.setUploadId(String.valueOf(tUpload.getId()));
        return initPartResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public com.github.ren.file.server.model.result.PartResult uploadMultipart(UploadPartRequest uploadPartRequest) {
        TUpload tUpload = checkMultipart(uploadPartRequest);
        String uploadId = tUpload.getUploadId();
        String objectName = tUpload.getObjectName();
        Integer partNumber = uploadPartRequest.getPartNumber();
        MultipartFile file = uploadPartRequest.getFile();
        long currentSize = file.getSize();
        try {
            UploadMultiPartArgs uploadMultiPartArgs = UploadMultiPartArgs.builder()
                    .uploadId(uploadId)
                    .objectName(objectName)
                    .partNumber(partNumber)
                    .inputStream(file.getInputStream())
                    .partSize(currentSize)
                    .build();
            PartResult partResultDTO = multipartUploadAdapter.uploadMultipart(storage, uploadMultiPartArgs);
            tUploadService.saveMultipart(tUpload, partResultDTO);

            com.github.ren.file.server.model.result.PartResult partResult = new com.github.ren.file.server.model.result.PartResult();
            partResult.setPartNumber(partResultDTO.getPartNumber());
            partResult.setPartSize(partResultDTO.getPartSize());
            partResult.setETag(partResultDTO.getETag());
            return partResult;
        } catch (IOException e) {
            log.error("分片上传失败", e);
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteMultipartResponse completeMultipart(CompletePartRequest request) {
        TUpload tUpload = checkUploadId(request.getUploadId());
        String objectName = tUpload.getObjectName();
        List<PartResult> multiParts = getMultiParts(tUpload);
        Long fileSize = tUpload.getFileSize();
        Long partSize = tUpload.getPartSize();
        int maxPartCount = getMaxPartCount(fileSize, partSize);
        //判断分片数量是否有误
        int size = multiParts.size();
        if (size != maxPartCount - 1 && size != maxPartCount) {
            throw new ApiException(ErrorCode.FILE_PART_COUNT_ERROR);
        }

        //判断分片总大小是否有误
        long totalSize = multiParts.stream()
                .mapToLong(PartResult::getPartSize)
                .sum();
        if (fileSize != totalSize) {
            throw new ApiException(ErrorCode.FILE_PART_TOTAL_SIZE_ERROR);
        }

        CompleteMultiPartArgs completeMultiPartArgs = CompleteMultiPartArgs.builder()
                .uploadId(tUpload.getUploadId())
                .objectName(objectName)
                .parts(multiParts)
                .build();

        CompleteMultipartResponse completeMultipartResponse = multipartUploadAdapter.completeMultipartUpload(storage, completeMultiPartArgs);
        tUploadService.completeUpload(request.getUploadId());
        log.info("完成上传 uploadId={} objectName={} result={}", request.getUploadId(), objectName, completeMultipartResponse);
        TFile tFile = new TFile();
        tFile.setFilesize(fileSize);
        tFile.setCreateTime(LocalDateTime.now());
        tFile.setEtag(completeMultipartResponse.getETag());
        tFile.setObjectName(completeMultipartResponse.getObjectName());
        tFile.setMd5(request.getMd5());
        tFile.setStatus(1);
        tFileService.save(tFile);
        return completeMultipartResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void abortMultipart(AbortPartRequest request) {
        TUpload tUpload = checkUploadId(request.getUploadId());
        String uploadId = tUpload.getUploadId();
        String objectName = tUpload.getObjectName();
        AbortMultiPartArgs abortMultiPartArgs = AbortMultiPartArgs.builder()
                .uploadId(uploadId)
                .objectName(objectName)
                .build();
        multipartUploadAdapter.abortMultipartUpload(storage, abortMultiPartArgs);
        tUploadService.abortUpload(uploadId);
    }
}
