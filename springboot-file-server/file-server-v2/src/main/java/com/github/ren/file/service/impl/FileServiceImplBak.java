//package com.github.ren.file.service.impl;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.github.ren.file.config.FileServerProperties;
//import com.github.ren.file.entity.TFile;
//import com.github.ren.file.entity.TUpload;
//import com.github.ren.file.ex.ApiException;
//import com.github.ren.file.model.ErrorCode;
//import com.github.ren.file.model.request.*;
//import com.github.ren.file.model.result.CheckResult;
//import com.github.ren.file.model.result.InitPartResult;
//import com.github.ren.file.sdk.FileClient;
//import com.github.ren.file.sdk.model.UploadGenericResult;
//import com.github.ren.file.sdk.objectname.UuidGenerator;
//import com.github.ren.file.sdk.part.*;
//import com.github.ren.file.service.FileService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @Description 文件接口逻辑类
// * @Author ren
// * @Since 1.0
// */
//@Service
//@Slf4j
//public class FileServiceImplBak implements FileService {
//
//    @Autowired
//    private FileClient fileClient;
//
//    @Autowired
//    private TFileServiceImpl tFileService;
//
//    @Autowired
//    private TUploadServiceImpl tUploadService;
//
//    private long maxUploadSize = 1024 * 1024 * 5;
//
//    private long multipartMinSize = 1024 * 100;
//
//    private long multipartMaxSize = 1024 * 1024 * 100;
//
//    private final int storage;
//
//    public FileServiceImplBak(FileServerProperties fileServerProperties) {
//        long maxUploadSize = fileServerProperties.getMaxUploadSize().toBytes();
//        if (maxUploadSize != 0) {
//            this.maxUploadSize = multipartMaxSize;
//        }
//
//        long multipartMaxSize = fileServerProperties.getMultipartMaxSize().toBytes();
//        if (multipartMaxSize != 0) {
//            this.multipartMaxSize = multipartMaxSize;
//        }
//
////        long multipartMinSize = fileServerProperties.getMultipartMinSize().toBytes();
////        if (multipartMinSize != 0) {
////            this.multipartMinSize = multipartMinSize;
////        }
//        this.multipartMinSize = fileServerProperties.getMinPartSize();
//        this.storage = fileServerProperties.getType();
//    }
//
//    public String objectName(String filename) {
//        return new UuidGenerator(filename).generator();
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String upload(SimpleUploadRequest request) {
//        MultipartFile file = request.getFile();
//        if (file.getSize() > maxUploadSize) {
//            throw new ApiException(ErrorCode.FILE_TO_LARGE);
//        }
//        try {
//            String objectName = objectName(file.getOriginalFilename());
//            UploadGenericResult result = fileClient.upload(file.getInputStream(), objectName);
//            objectName = result.getObjectName();
//            String eTag = result.getETag();
//            TFile tFile = new TFile();
//            tFile.setFilesize(file.getSize());
//            tFile.setCreateTime(LocalDateTime.now());
//            tFile.setEtag(eTag);
//            tFile.setObjectname(objectName);
//            tFile.setMd5(eTag);
//            tFile.setStatus(1);
//            tFileService.save(tFile);
//            return objectName;
//        } catch (IOException e) {
//            log.error("上传失败", e);
//            throw new ApiException(ErrorCode.UPLOAD_ERROR);
//        }
//    }
//
//    @Override
//    public CheckResult check(CheckRequest request) {
//        CheckResult checkResult = new CheckResult();
//        String md5 = request.getMd5();
//        if (StringUtils.isNotBlank(md5)) {
//            TFile tFile = tFileService.getBaseMapper().selectOne(Wrappers.<TFile>lambdaQuery().eq(TFile::getMd5, md5).last(" limit 1"));
//            if (tFile != null) {
//                checkResult.setExist(true);
//            }
//        }
//        String uploadId = request.getUploadId();
//        if (StringUtils.isNotBlank(uploadId)) {
//            TUpload tUpload = checkUploadId(uploadId);
//            String objectName = tUpload.getObjectName();
//            ListMultipartUploadArgs args = ListMultipartUploadArgs.builder().uploadId(uploadId).objectName(objectName).build();
//            List<UploadMultipartResponse> uploadMultipartResponses = fileClient.listMultipartUpload(args);
//            checkResult.setParts(uploadMultipartResponses);
//        } else {
//            checkResult.setParts(new ArrayList<>(0));
//        }
//        return checkResult;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public InitPartResult initiateMultipartUpload(InitPartRequest request) {
//        //查询过期时间
//        LocalDateTime expire = getExpire();
//        String filename = request.getFilename();
////        Long partSize = request.getPartsize();
//        Long filesize = request.getFilesize();
////        if (partSize < multipartMinSize || partSize > multipartMaxSize) {
////            throw new ApiException(ErrorCode.FILE_PART_SIZE_ERROR,
////                    "必须在" + multipartMinSize / 1024 / 1024 + "M~" + multipartMaxSize / 1024 / 1024 + "M之间");
////        }
//        String md5 = request.getMd5();
//
//        String objectName = this.objectName(FilenameUtils.getExtension(filename));
//        InitMultipartUploadArgs args = InitMultipartUploadArgs.builder()
//                .objectName(objectName)
//                .fileSize(filesize).build();
//        InitMultipartResponse initMultipartResponse = fileClient.initMultipartUpload(args);
//        InitPartResult initPartResult = new InitPartResult();
//        initPartResult.setUploadId(initMultipartResponse.getUploadId());
//        TUpload tUpload = new TUpload();
//        tUpload.setUploadId(initPartResult.getUploadId());
//        tUpload.setObjectName(initMultipartResponse.getObjectName());
//        tUpload.setCreateTime(LocalDateTime.now());
//        tUpload.setExpireAt(expire);
//        tUpload.setStorage(storage);
////        tUpload.setFileSize(filesize);
////        tUpload.setPartSize(partSize);
////        int partCount = Math.max(1, (int) Math.ceil(filesize / (float) partSize));
////        tUpload.setPartCount(partCount);
//        tUpload.setStatus(0);
//        tUploadService.getBaseMapper().insert(tUpload);
//        return initPartResult;
//    }
//
//    private LocalDateTime getExpire() {
//        int partExpirationDays = fileClient.getPartExpirationDays();
//        if (partExpirationDays == -1) {
//            return null;
//        }
//        Date date = DateUtils.addDays(new Date(), partExpirationDays);
//        Instant instant = date.toInstant();
//        ZoneId zoneId = ZoneId.systemDefault();
//        return instant.atZone(zoneId).toLocalDateTime();
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public UploadMultipartResponse uploadPart(UploadPartRequest uploadPartRequest) {
//        MultipartFile file = uploadPartRequest.getFile();
//        String uploadId = uploadPartRequest.getUploadId();
//        TUpload tUpload = checkUploadId(uploadId);
//        String objectName = tUpload.getObjectName();
//        Integer partNumber = uploadPartRequest.getPartNumber();
//        long size = file.getSize();
////        if (!partNumber.equals(tUpload.getPartCount())) {
//        if (size > multipartMaxSize) {
//            throw new ApiException(ErrorCode.FILE_PART_SIZE_ERROR, "必须小于" + multipartMaxSize / 1024 / 1024 + "M");
//        }
////        }
//        try {
//            UploadPartArgs part = new UploadPartArgs(uploadId, objectName, partNumber, size, file.getInputStream());
//            return fileClient.uploadMultipart(part);
//        } catch (IOException e) {
//            log.error("分片上传失败", e);
//            throw new ApiException(ErrorCode.UPLOAD_ERROR);
//        }
//    }
//
//    private TUpload checkUploadId(String uploadId) {
//        //TODO 缓存加入
//        TUpload tUpload = tUploadService.getBaseMapper().selectOne(
//                Wrappers.<TUpload>lambdaQuery().eq(TUpload::getUploadId, uploadId)
//                        .eq(TUpload::getStorage, storage)
//        );
//        if (tUpload == null || tUpload.getStatus() != 0) {
//            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
//        }
//        LocalDateTime expireAt = tUpload.getExpireAt();
//        if (expireAt != null && LocalDateTime.now().isAfter(expireAt)) {
//            throw new ApiException(ErrorCode.UPLOAD_ID_EXPIRE);
//        }
//        return tUpload;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public CompleteMultipartResponse completeMultipartUpload(CompletePartRequest request) {
//        String uploadId = request.getUploadId();
//        TUpload tUpload = checkUploadId(uploadId);
//        String objectName = tUpload.getObjectName();
//        ListMultipartUploadArgs args = ListMultipartUploadArgs.builder().uploadId(uploadId).objectName(objectName).build();
//        List<UploadMultipartResponse> uploadMultipartResponses = fileClient.listMultipartUpload(args);
//        if (uploadMultipartResponses.isEmpty()) {
//            throw new ApiException(ErrorCode.FILE_PART_ISEMPTY);
//        }
//        if (uploadMultipartResponses.size() > 1) {
//            long count = uploadMultipartResponses.stream()
//                    .filter(p -> p.getPartSize() < multipartMinSize)
//                    .limit(uploadMultipartResponses.size() - 1)
//                    .count();
//            if (count > 0) {
//                throw new ApiException(ErrorCode.FILE_PART_SIZE_ERROR, "所有分片必须大于" + multipartMinSize / 1024 + "KB");
//            }
//        }
////        if (tUpload.getPartCount() != partInfos.size()) {
////            throw new ApiException(ErrorCode.FILE_PART_COUNT_ERROR);
////        }
//        long fileSize = uploadMultipartResponses.stream().mapToLong(UploadMultipartResponse::getPartSize).sum();
////        if (tUpload.getFileSize() != fileSize) {
////            throw new ApiException(ErrorCode.FILE_PART_TOTAL_SIZE_ERROR);
////        }
//        CompleteMultipartResponse completeMultipartResponse = fileClient.completeMultipartUpload(uploadId, objectName, uploadMultipartResponses);
//        TFile tFile = new TFile();
//        tFile.setFilesize(fileSize);
//        tFile.setCreateTime(LocalDateTime.now());
//        tFile.setEtag(completeMultipartResponse.getETag());
//        tFile.setObjectname(completeMultipartResponse.getObjectName());
//        tFile.setMd5(request.getMd5());
//        tFile.setStatus(1);
//        tFileService.save(tFile);
//        tUpload.setStatus(1);
//        tUploadService.getBaseMapper().updateById(tUpload);
//        return completeMultipartResponse;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void abortMultipartUpload(AbortPartRequest request) {
//        String uploadId = request.getUploadId();
//        TUpload tUpload = checkUploadId(uploadId);
//        String objectName = tUpload.getObjectName();
//        fileClient.abortMultipartUpload(uploadId, objectName);
//        tUpload.setStatus(-1);
//        tUploadService.getBaseMapper().updateById(tUpload);
//    }
//}
