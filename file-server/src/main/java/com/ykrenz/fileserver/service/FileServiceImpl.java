package com.ykrenz.fileserver.service;

import com.ykrenz.fileserver.config.StorageProperties;
import com.ykrenz.fileserver.entity.FileInfo;
import com.ykrenz.fileserver.ex.ApiException;
import com.ykrenz.fileserver.model.ErrorCode;
import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.FileInfoRequest;
import com.ykrenz.fileserver.model.request.InitUploadMultipartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadMultipartRequest;
import com.ykrenz.fileserver.model.result.FileInfoResult;
import com.ykrenz.fileserver.model.result.InitMultipartResult;
import com.ykrenz.fileserver.service.client.FileServerClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author ykren
 * @date 2022/3/4
 */
@Service
public class FileServiceImpl implements FileService {

    private StorageProperties storageProperties;

    private FileServerClient fileServerClient;

    private final long maxUploadSize;
    private final long multipartMinSize;
    private final long multipartMaxSize;

    public FileServiceImpl(StorageProperties storageProperties, FileServerClient fileServerClient) {
        this.storageProperties = storageProperties;
        this.fileServerClient = fileServerClient;
        this.maxUploadSize = storageProperties.getMaxUploadSize().toBytes();
        this.multipartMinSize = storageProperties.getMultipartMinSize().toBytes();
        this.multipartMaxSize = storageProperties.getMultipartMaxSize().toBytes();
    }

    @Override
    public FileInfoResult upload(SimpleUploadRequest request) {
        try {
            // 限制文件大小
            if (request.getFile().getSize() > maxUploadSize) {
                String msg = String.format("文件限制%dM,请使用分片上传", maxUploadSize / 1024 / 1024);
                throw new ApiException(msg);
            }
            FileInfo fileInfo = fileServerClient.upload(request);
            if (request.isInfo()) {
                return info(new FileInfoRequest(fileInfo.getBucketName(), fileInfo.getObjectName()));
            }
            return new FileInfoResult();
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    public InitMultipartResult initMultipart(InitUploadMultipartRequest request) {
        Long partSize = request.getPartSize();
        if (partSize < multipartMinSize || partSize > multipartMaxSize) {
            String msg = String.format("分片大小必须在%dM~%dM之间", multipartMinSize / 1024 / 1024, multipartMaxSize / 1024 / 1024);
            throw new ApiException(msg);
        }
        return fileServerClient.initMultipart(request);
    }

    @Override
    public void uploadMultipart(UploadMultipartRequest request) {
        try {
            // 前端index=0
            request.setPartNumber(request.getPartNumber() + 1);
            fileServerClient.uploadMultipart(request);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    @Override
    public FileInfoResult completeMultipart(CompletePartRequest request) {
        FileInfo fileInfo = fileServerClient.completeMultipart(request);
        if (request.isInfo()) {
            return info(new FileInfoRequest(fileInfo.getBucketName(), fileInfo.getObjectName()));
        }
        return new FileInfoResult();
    }

    @Override
    public void cancelMultipart(CancelPartRequest request) {
        fileServerClient.cancelMultipart(request);
    }

    @Override
    public FileInfoResult info(FileInfoRequest request) {
        return fileServerClient.info(request);
    }

    @Override
    public void deleteAllFiles() {
        fileServerClient.deleteAllFiles();
    }

}
