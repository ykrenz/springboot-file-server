package com.github.ren.file.service;

import com.github.ren.file.ex.ApiException;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.request.AbortPartRequest;
import com.github.ren.file.model.request.CompletePartRequest;
import com.github.ren.file.model.request.InitPartRequest;
import com.github.ren.file.model.request.SimpleUploadRequest;
import com.github.ren.file.model.request.UploadPartRequest;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.service.impl.FileServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author ykren
 * @date 2022/3/4
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * 5M
     */
    long simpleMaxSize = 1024L * 1024L * 5L;

    @Autowired
    FileServerClient fileServerClient;

    @Override
    public String upload(SimpleUploadRequest request) {
        try {
            // 限制文件大小
            if (request.getFile().getSize() > simpleMaxSize) {
                throw new ApiException(ErrorCode.FILE_TO_LARGE);
            }
            fileServerClient.upload(request);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
        return null;
    }

    @Override
    public InitPartResult initMultipart(InitPartRequest request) {
        return fileServerClient.initMultipart(request);
    }

    @Override
    public void uploadMultipart(UploadPartRequest request) {
        try {
            // 前端index=0
            request.setPartNumber(request.getPartNumber() + 1);
            fileServerClient.uploadMultipart(request);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    public void completeMultipart(CompletePartRequest request) {
        fileServerClient.completeMultipart(request);
    }

    @Override
    public void abortMultipart(AbortPartRequest request) {
        fileServerClient.abortMultipart(request);
    }

    @Override
    public void deleteAllFiles() {
        fileServerClient.deleteAllFiles();
    }
}
