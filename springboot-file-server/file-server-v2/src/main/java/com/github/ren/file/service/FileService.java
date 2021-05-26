package com.github.ren.file.service;

import com.github.ren.file.model.request.*;
import com.github.ren.file.model.result.CheckResult;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;

import java.util.List;

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
     * 查询上传任务
     *
     * @param request
     * @return
     */
    List<InitPartResult> listMultipartUpload(ListPartRequest request);

    /**
     * 初始化分片上传任务
     *
     * @param request
     * @return
     */
    InitPartResult initiateMultipartUpload(InitPartRequest request);

    /**
     * 上传文件分片
     *
     * @param uploadPartRequest
     * @return
     */
    PartInfo uploadPart(UploadPartRequest uploadPartRequest);

    /**
     * 合并文件分片
     *
     * @param request
     * @return
     */
    CompleteMultipart completeMultipartUpload(CompletePartRequest request);

    /**
     * 取消分片上传
     *
     * @param uploadId
     * @return
     */
    String abortMultipartUpload(AbortPartRequest request);

}
