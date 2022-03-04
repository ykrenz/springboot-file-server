package com.github.ren.file.service.impl;

import com.github.ren.file.entity.FileInfo;
import com.github.ren.file.entity.FilePartInfo;
import com.github.ren.file.model.request.AbortPartRequest;
import com.github.ren.file.model.request.CompletePartRequest;
import com.github.ren.file.model.request.InitPartRequest;
import com.github.ren.file.model.request.SimpleUploadRequest;
import com.github.ren.file.model.request.UploadPartRequest;
import com.github.ren.file.model.result.InitPartResult;

import java.io.IOException;

/**
 * @author ykren
 * @date 2022/3/1
 */
public interface FileServerClient {

    /**
     * 上传文件
     *
     * @param request
     * @return
     * @throws IOException
     */
    FileInfo upload(SimpleUploadRequest request) throws IOException;

    /**
     * 初始化分片
     *
     * @param request
     * @return
     */
    InitPartResult initMultipart(InitPartRequest request);

    /**
     * 上传分片
     *
     * @param request
     * @return
     * @throws IOException
     */
    FilePartInfo uploadMultipart(UploadPartRequest request) throws IOException;

    /**
     * 完成分片上传
     *
     * @param request
     * @return
     */
    FileInfo completeMultipart(CompletePartRequest request);

    /**
     * 取消分片上传
     *
     * @param request
     */
    void abortMultipart(AbortPartRequest request);

    /**
     * 删除数据库所有文件
     */
    void deleteAllFiles();

}
