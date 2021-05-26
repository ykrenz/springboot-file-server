package com.github.ren.file.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.config.FileServerProperties;
import com.github.ren.file.entity.TFile;
import com.github.ren.file.entity.TPart;
import com.github.ren.file.ex.ApiException;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.request.*;
import com.github.ren.file.model.result.CheckResult;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.model.UploadGenericResult;
import com.github.ren.file.sdk.objectname.UuidGenerator;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.InitMultipartResult;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.UploadPart;
import com.github.ren.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private FileClient fileClient;

    @Autowired
    private TPartServiceImpl tPartService;

    @Autowired
    private TFileServiceImpl tFileService;

    private long maxUploadSize = 1024 * 1024 * 5;

    private long multipartMinSize = 1024 * 1024 * 5;

    private long multipartMaxSize = 1024 * 1024 * 100;

    public FileServiceImpl(FileServerProperties fileServerProperties) {
        long maxUploadSize = fileServerProperties.getMaxUploadSize().toBytes();
        if (maxUploadSize != 0) {
            this.maxUploadSize = multipartMaxSize;
        }

        long multipartMinSize = fileServerProperties.getMultipartMinSize().toBytes();
        if (multipartMinSize != 0) {
            this.multipartMinSize = multipartMinSize;
        }

        long multipartMaxSize = fileServerProperties.getMultipartMaxSize().toBytes();
        if (multipartMaxSize != 0) {
            this.multipartMaxSize = multipartMaxSize;
        }
    }

    public String objectName(String filename) {
        return new UuidGenerator(filename).generator();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(SimpleUploadRequest request) {
        MultipartFile file = request.getFile();
        if (file.getSize() > maxUploadSize) {
            throw new ApiException(ErrorCode.FILE_TO_LARGE);
        }
        try {
            String objectName = objectName(file.getOriginalFilename());
            UploadGenericResult result = fileClient.upload(file.getInputStream(), objectName);
            objectName = result.getObjectName();
            String eTag = result.getETag();
            TFile tFile = new TFile();
            tFile.setFilesize(file.getSize());
            tFile.setCreateTime(LocalDateTime.now());
            tFile.setEtag(eTag);
            tFile.setObjectname(objectName);
            tFile.setMd5(eTag);
            tFile.setStatus(1);
            tFileService.save(tFile);
            return objectName;
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    public CheckResult check(CheckRequest request) {
        String md5 = request.getMd5();
        CheckResult checkResult = new CheckResult();
        TFile tFile = tFileService.getBaseMapper().selectOne(Wrappers.<TFile>lambdaQuery().eq(TFile::getMd5, md5).last(" limit 1"));
        if (tFile != null) {
            checkResult.setExist(true);
        }

        return null;
    }

    @Override
    public List<InitPartResult> listMultipartUpload(ListPartRequest request) {
        return null;
    }

    @Override
    public InitPartResult initiateMultipartUpload(InitPartRequest request) {
        String filename = request.getFilename();
        String objectName = this.objectName(filename);
        InitMultipartResult initMultipartResult = fileClient.initiateMultipartUpload(objectName);
        String uploadId = initMultipartResult.getUploadId();
        objectName = initMultipartResult.getObjectName();
        TPart tPart = new TPart();
        tPart.setUploadId(uploadId);
        tPart.setPartNumber(0);
        tPart.setPartSize(0L);
        tPart.setCreateTime(LocalDateTime.now());
        tPart.setStatus(1);
        tPart.setObjectName(objectName);
        tPartService.save(tPart);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PartInfo uploadPart(UploadPartRequest uploadPartRequest) {
        MultipartFile file = uploadPartRequest.getFile();
        long size = file.getSize();
        if (size < multipartMinSize || size > multipartMaxSize) {
            throw new ApiException(ErrorCode.FILE_PART_SIZE_ERROR);
        }
        String uploadId = uploadPartRequest.getUploadId();
        TPart tPart = tPartService.getBaseMapper().selectOne(Wrappers.<TPart>lambdaQuery().eq(TPart::getUploadId, uploadId));
        if (tPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        try {
            Integer partNumber = uploadPartRequest.getPartNumber();
            String objectName = tPart.getObjectName();
            UploadPart part = new UploadPart(uploadId, objectName, partNumber, size, file.getInputStream());
            PartInfo partInfo = fileClient.uploadPart(part);
            tPart = new TPart();
            tPart.setUploadId(uploadId);
            tPart.setPartNumber(partInfo.getPartNumber());
            tPart.setPartSize(partInfo.getPartSize());
            tPart.setETag(partInfo.getETag());
            tPart.setObjectName(objectName);
            tPart.setCreateTime(LocalDateTime.now());
            tPart.setStatus(1);
            tPartService.save(tPart);
            return partInfo;
        } catch (IOException e) {
            log.error("分片上传失败", e);
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    public CompleteMultipart completeMultipartUpload(CompletePartRequest request) {
        String uploadId = request.getUploadId();
        TPart tPart = tPartService.getBaseMapper().selectOne(Wrappers.<TPart>lambdaQuery().eq(TPart::getUploadId, uploadId));
        if (tPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        String objectName = tPart.getObjectName();
        List<PartInfo> partInfos = fileClient.listParts(uploadId, objectName);
        CompleteMultipart completeMultipart = fileClient.completeMultipartUpload(uploadId, objectName, partInfos);
        return completeMultipart;
    }

    @Override
    public String abortMultipartUpload(AbortPartRequest request) {
        return null;
    }
}
