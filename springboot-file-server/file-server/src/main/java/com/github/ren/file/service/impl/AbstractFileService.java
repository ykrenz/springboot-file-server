package com.github.ren.file.service.impl;

import com.github.ren.file.entity.BaseEntity;
import com.github.ren.file.ex.ApiException;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.request.CompletePartRequest;
import com.github.ren.file.model.request.InitPartRequest;
import com.github.ren.file.model.request.SimpleUploadRequest;
import com.github.ren.file.model.request.UploadPartRequest;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Slf4j
public abstract class AbstractFileService<F extends BaseEntity, P extends BaseEntity> implements FileService {

    public abstract F uploadFile(SimpleUploadRequest request) throws IOException;

    public abstract P initUploadPart(InitPartRequest request);

    public abstract P uploadPart(UploadPartRequest request) throws IOException;

    public abstract F completePart(CompletePartRequest request);

    /**
     * 5M
     */
    long simpleMaxSize = 1024L * 1024L * 5L;

    @Override
    public String upload(SimpleUploadRequest request) {
        try {
            // 限制文件大小
            if (request.getFile().getSize() > simpleMaxSize) {
                throw new ApiException(ErrorCode.FILE_TO_LARGE);
            }
            F f = uploadFile(request);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initMultipart(InitPartRequest request) {
        P part = initUploadPart(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadMultipart(UploadPartRequest request) {
        try {
            P part = uploadPart(request);
            System.out.println(part);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeMultipart(CompletePartRequest request) {
        F f = completePart(request);
        f.setCreateTime(LocalDateTime.now());
        f.setUpdateTime(LocalDateTime.now());
        f.setStatus(1);
        System.out.println(f);
    }


}
