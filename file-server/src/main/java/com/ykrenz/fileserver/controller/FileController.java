package com.ykrenz.fileserver.controller;

import com.ykrenz.fileserver.model.ResultUtil;
import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.InitPartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadPartRequest;
import com.ykrenz.fileserver.model.result.InitPartResult;
import com.ykrenz.fileserver.model.result.CompletePartResult;
import com.ykrenz.fileserver.model.result.SimpleUploadResult;
import com.ykrenz.fileserver.service.FileService;
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
    public ResultUtil<SimpleUploadResult> upload(@Validated SimpleUploadRequest request) {
        return ResultUtil.success(fileService.upload(request));
    }

    @ApiOperation("初始化分片上传 秒传和断点续传")
    @ApiOperationSupport(order = 3)
    @PostMapping("/initMultipart")
    public ResultUtil<InitPartResult> initMultipart(@Validated InitPartRequest request) {
        return ResultUtil.success(fileService.initMultipart(request));
    }

    @ApiOperation("上传文件分片")
    @ApiOperationSupport(order = 4)
    @PostMapping("/uploadMultipart")
    public ResultUtil<Object> uploadMultipart(@Validated UploadPartRequest uploadPartRequest) {
        fileService.uploadMultipart(uploadPartRequest);
        return ResultUtil.success();
    }

    @ApiOperation("合并文件分片")
    @ApiOperationSupport(order = 5)
    @PostMapping("/completeMultipart")
    public ResultUtil<CompletePartResult> completeMultipart(@Validated CompletePartRequest request) {
        return ResultUtil.success(fileService.completeMultipart(request));
    }

    @ApiOperation("取消分片上传")
    @ApiOperationSupport(order = 6)
    @PostMapping("/cancelMultipart")
    public ResultUtil<Object> cancelMultipart(@Validated CancelPartRequest request) {
        fileService.cancelMultipart(request);
        return ResultUtil.success();
    }

//    @ApiOperation("删除所有文件-测试使用")
//    @ApiOperationSupport(order = 8)
//    @PostMapping("/deleteAllFiles")
//    public ResultUtil<Object> deleteAllFiles() {
//        fileService.deleteAllFiles();
//        return ResultUtil.success();
//    }
}
