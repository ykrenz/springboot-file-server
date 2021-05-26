package com.github.ren.file.controller;

import com.github.ren.file.model.ResultUtil;
import com.github.ren.file.model.request.*;
import com.github.ren.file.model.result.CheckResult;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.service.FileService;
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

    @ApiOperation("简单上传 5M以下文件")
    @PostMapping("/upload")
    public ResultUtil<String> upload(@Validated SimpleUploadRequest request) {
        return ResultUtil.success(fileService.upload(request));
    }

    @ApiOperation("检测上传 秒传和断点续传")
    @PostMapping("/check")
    public ResultUtil<CheckResult> check(@Validated CheckRequest request) {
        return ResultUtil.success(fileService.check(request));
    }

    @ApiOperation("初始化分片上传")
    @PostMapping("/initMultipartUpload")
    public ResultUtil<InitPartResult> initiateMultipartUpload(@Validated InitPartRequest request) {
        return ResultUtil.success(fileService.initiateMultipartUpload(request));
    }

    @ApiOperation("上传文件分片")
    @PostMapping("/uploadMultipart")
    public ResultUtil<PartInfo> uploadPart(@Validated UploadPartRequest uploadPartRequest) {
        return ResultUtil.success(fileService.uploadPart(uploadPartRequest));
    }

    @ApiOperation("合并文件分片")
    @PostMapping("/completeMultipartUpload")
    public ResultUtil<CompleteMultipart> completeMultipartUpload(@Validated CompletePartRequest request) {
        return ResultUtil.success(fileService.completeMultipartUpload(request));
    }

    @ApiOperation("取消分片上传")
    @PostMapping("/abortMultipartUpload")
    public ResultUtil<String> abortMultipartUpload(@Validated AbortPartRequest request) {
        return ResultUtil.success(fileService.abortMultipartUpload(request));
    }
}
