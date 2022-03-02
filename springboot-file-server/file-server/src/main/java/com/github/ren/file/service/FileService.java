package com.github.ren.file.service;

import com.github.ren.file.model.request.AbortPartRequest;
import com.github.ren.file.model.request.CheckRequest;
import com.github.ren.file.model.request.CompletePartRequest;
import com.github.ren.file.model.request.InitPartRequest;
import com.github.ren.file.model.request.SimpleUploadRequest;
import com.github.ren.file.model.request.UploadPartRequest;
import com.github.ren.file.model.result.CheckResult;
import com.github.ren.file.model.result.InitPartResult;

/**
 * @Description 文件接口
 * @Author ren
 * @Since 1.0
 */
public interface FileService {

    /**
     * 上传简单文件
     *
     * @param request
     * @return
     */
    String upload(SimpleUploadRequest request);

    /**
     * 检测文件 秒传
     *
     * @param request
     * @return
     */
    CheckResult check(CheckRequest request);

    /**
     * 初始化分片上传任务
     *
     * @param request
     * @return
     */
    InitPartResult initMultipart(InitPartRequest request);

    /**
     * 上传文件分片
     *
     * @param uploadPartRequest
     * @return
     */
    void uploadMultipart(UploadPartRequest uploadPartRequest);

    /**
     * 合并文件分片
     *
     * @param request
     * @return
     */
    void completeMultipart(CompletePartRequest request);

    /**
     * 取消分片上传
     *
     * @param request
     * @return
     */
    void abortMultipart(AbortPartRequest request);

    /**
     * 清空临时文件 测试使用
     */
    void deleteAllFiles();

}
