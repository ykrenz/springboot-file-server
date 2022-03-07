package com.ykrenz.fileserver.service;

import com.ykrenz.fileserver.entity.FileInfo;
import com.ykrenz.fileserver.ex.ApiException;
import com.ykrenz.fileserver.model.ErrorCode;
import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.InitPartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadPartRequest;
import com.ykrenz.fileserver.model.result.InitPartResult;
import com.ykrenz.fileserver.model.result.CompletePartResult;
import com.ykrenz.fileserver.model.result.SimpleUploadResult;
import com.ykrenz.fileserver.service.impl.FileServerClient;
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
    private FileServerClient fileServerClient;

    @Override
    public SimpleUploadResult upload(SimpleUploadRequest request) {
        try {
            // 限制文件大小
            if (request.getFile().getSize() > simpleMaxSize) {
                throw new ApiException(ErrorCode.FILE_TO_LARGE);
            }
            FileInfo fileInfo = fileServerClient.upload(request);
            return new SimpleUploadResult(fileInfo);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
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
    public CompletePartResult completeMultipart(CompletePartRequest request) {
        FileInfo fileInfo = fileServerClient.completeMultipart(request);
        return new CompletePartResult(request.getUploadId(), fileInfo);
    }

    @Override
    public void cancelMultipart(CancelPartRequest request) {
        fileServerClient.cancelMultipart(request);
    }

    @Override
    public void deleteAllFiles() {
        fileServerClient.deleteAllFiles();
    }

}
