package com.github.ren.file.controller;

import com.github.ren.file.model.ResultUtil;
import com.github.ren.file.model.request.*;
import com.github.ren.file.model.result.CheckResult;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.sdk.part.CompleteMultipartResponse;
import com.github.ren.file.sdk.part.UploadMultipartResponse;
import com.github.ren.file.service.FileService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 文件接口
 * @Author ren
 * @Since 1.0
 */
@RestController
@Api(tags = "文件")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("简单上传 只支持小文件上传")
    @ApiOperationSupport(order = 1)
    @PostMapping("/upload")
    public ResultUtil<String> upload(@Validated SimpleUploadRequest request) {
        return ResultUtil.success(fileService.upload(request));
    }

    @ApiOperation("检测上传 秒传和断点续传")
    @ApiOperationSupport(order = 2)
    @PostMapping("/check")
    public ResultUtil<CheckResult> check(@Validated CheckRequest request) {
        return ResultUtil.success(fileService.check(request));
    }

    @ApiOperation("初始化分片上传")
    @ApiOperationSupport(order = 3)
    @PostMapping("/initMultipartUpload")
    public ResultUtil<InitPartResult> initiateMultipartUpload(@Validated InitPartRequest request) {
        return ResultUtil.success(fileService.initiateMultipartUpload(request));
    }

    @ApiOperation("上传文件分片")
    @ApiOperationSupport(order = 4)
    @PostMapping("/uploadMultipart")
    public ResultUtil<UploadMultipartResponse> uploadPart(@Validated UploadPartRequest uploadPartRequest) {
        return ResultUtil.success(fileService.uploadPart(uploadPartRequest));
    }

    @ApiOperation("合并文件分片")
    @ApiOperationSupport(order = 5)
    @PostMapping("/completeMultipartUpload")
    public ResultUtil<CompleteMultipartResponse> completeMultipartUpload(@Validated CompletePartRequest request) {
        return ResultUtil.success(fileService.completeMultipartUpload(request));
    }

    @ApiOperation("取消分片上传")
    @ApiOperationSupport(order = 6)
    @PostMapping("/abortMultipartUpload")
    public ResultUtil<String> abortMultipartUpload(@Validated AbortPartRequest request) {
        fileService.abortMultipartUpload(request);
        return ResultUtil.success();
    }
}
