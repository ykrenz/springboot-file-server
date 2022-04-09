package com.ykrenz.file.service;

import com.ykrenz.file.model.result.InitUploadMultipartResult;
import com.ykrenz.file.model.request.*;
import com.ykrenz.file.model.result.FileResult;
import com.ykrenz.file.model.result.ListMultipartResult;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description 文件接口
 * @Author ren
 * @Since 1.0
 */
public interface FileService extends Serializable {

    /**
     * 上传简单文件
     *
     * @param request
     * @return
     */
    FileResult upload(SimpleUploadRequest request) throws IOException;

    /**
     * 秒传
     *
     * @param request
     * @return
     */
    FileResult fastUpload(FastUploadRequest request);

    /**
     * 初始化分片上传任务
     *
     * @param request
     * @return
     */
    InitUploadMultipartResult initMultipart(InitUploadMultipartRequest request);

    /**
     * 查询已经上传的分片
     *
     * @param request
     * @return
     */
    ListMultipartResult listMultipart(ListMultipartRequest request);

    /**
     * 上传文件分片
     *
     * @param uploadMultipartRequest
     * @return
     */
    void uploadMultipart(UploadMultipartRequest uploadMultipartRequest) throws IOException;

    /**
     * 完成文件分片上传
     *
     * @param request
     * @return
     */
    FileResult completeMultipart(CompleteMultipartRequest request);

    /**
     * 取消分片上传
     *
     * @param request
     * @return
     */
    void cancelMultipart(CancelMultipartRequest request);

    /**
     * 查询文件信息
     *
     * @param id
     * @return
     */
    FileResult info(FileInfoRequest id);

    /**
     * 清空所有文件 测试使用
     */
    void deleteAllFiles();

}
