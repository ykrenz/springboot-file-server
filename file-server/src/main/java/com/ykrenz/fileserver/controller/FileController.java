package com.ykrenz.fileserver.controller;

import com.ykrenz.fileserver.model.ResultUtil;
import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.FileInfoRequest;
import com.ykrenz.fileserver.model.request.InitUploadMultipartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadMultipartRequest;
import com.ykrenz.fileserver.model.result.FileInfoResult;
import com.ykrenz.fileserver.model.result.InitMultipartResult;
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

    @ApiOperation(value = "简单上传", notes = "只支持小文件上传")
    @ApiOperationSupport(order = 1)
    @PostMapping("/upload")
    public ResultUtil<FileInfoResult> upload(@Validated SimpleUploadRequest request) {
        return ResultUtil.success(fileService.upload(request));
    }

    @ApiOperation("初始化分片上传+秒传+断点续传")
    @ApiOperationSupport(order = 2)
    @PostMapping("/initMultipart")
    public ResultUtil<InitMultipartResult> initMultipart(@Validated InitUploadMultipartRequest request) {
        return ResultUtil.success(fileService.initMultipart(request));
    }

    @ApiOperation("上传文件分片")
    @ApiOperationSupport(order = 3)
    @PostMapping("/uploadMultipart")
    public ResultUtil<Object> uploadMultipart(@Validated UploadMultipartRequest uploadMultipartRequest) {
        fileService.uploadMultipart(uploadMultipartRequest);
        return ResultUtil.success();
    }

    @ApiOperation("完成分片上传")
    @ApiOperationSupport(order = 4)
    @PostMapping("/completeMultipart")
    public ResultUtil<FileInfoResult> completeMultipart(@Validated CompletePartRequest request) {
        return ResultUtil.success(fileService.completeMultipart(request));
    }

    @ApiOperation("取消分片上传")
    @ApiOperationSupport(order = 5)
    @PostMapping("/cancelMultipart")
    public ResultUtil<Object> cancelMultipart(@Validated CancelPartRequest request) {
        fileService.cancelMultipart(request);
        return ResultUtil.success();
    }

    @ApiOperation("查询文件信息")
    @ApiOperationSupport(order = 6)
    @PostMapping("/info")
    public ResultUtil<FileInfoResult> info(@Validated FileInfoRequest request) {
        return ResultUtil.success(fileService.info(request));
    }

//    @ApiOperation("删除所有文件-测试使用")
//    @ApiOperationSupport(order = 8)
//    @PostMapping("/deleteAllFiles")
//    public ResultUtil<Object> deleteAllFiles() {
//        fileService.deleteAllFiles();
//        return ResultUtil.success();
//    }
}
