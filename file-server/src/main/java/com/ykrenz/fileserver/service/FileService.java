package com.ykrenz.fileserver.service;

import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.FileInfoRequest;
import com.ykrenz.fileserver.model.request.InitUploadMultipartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadMultipartRequest;
import com.ykrenz.fileserver.model.result.FileInfoResult;
import com.ykrenz.fileserver.model.result.InitMultipartResult;

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
    FileInfoResult upload(SimpleUploadRequest request);

    /**
     * 初始化分片上传任务
     *
     * @param request
     * @return
     */
    InitMultipartResult initMultipart(InitUploadMultipartRequest request);

    /**
     * 上传文件分片
     *
     * @param uploadMultipartRequest
     * @return
     */
    void uploadMultipart(UploadMultipartRequest uploadMultipartRequest);

    /**
     * 合并文件分片
     *
     * @param request
     * @return
     */
    FileInfoResult completeMultipart(CompletePartRequest request);

    /**
     * 取消分片上传
     *
     * @param request
     * @return
     */
    void cancelMultipart(CancelPartRequest request);

    /**
     * 查询文件信息
     *
     * @param id
     * @return
     */
    FileInfoResult info(FileInfoRequest id);

    /**
     * 清空所有文件 测试使用
     */
    void deleteAllFiles();

}
