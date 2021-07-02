package com.github.ren.file.controller;

import com.github.ren.file.model.ResultUtil;
import com.github.ren.file.model.request.*;
import com.github.ren.file.model.result.CheckResult;
import com.github.ren.file.model.result.InitPartResult;
import com.github.ren.file.model.result.PartResult;
import com.github.ren.file.sdk.part.CompleteMultipartResponse;
import com.github.ren.file.service.FileService;
import com.github.ren.file.service.impl.TUploadServiceImpl;
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
    @PostMapping("/initMultipart")
    public ResultUtil<InitPartResult> initMultipart(@Validated InitPartRequest request) {
        return ResultUtil.success(fileService.initMultipart(request));
    }

    @ApiOperation("上传文件分片")
    @ApiOperationSupport(order = 4)
    @PostMapping("/uploadMultipart")
    public ResultUtil<PartResult> uploadMultipart(@Validated UploadPartRequest uploadPartRequest) {
        return ResultUtil.success(fileService.uploadMultipart(uploadPartRequest));
    }

    @ApiOperation("合并文件分片")
    @ApiOperationSupport(order = 5)
    @PostMapping("/completeMultipart")
    public ResultUtil<CompleteMultipartResponse> completeMultipart(@Validated CompletePartRequest request) {
        return ResultUtil.success(fileService.completeMultipart(request));
    }

    @ApiOperation("取消分片上传")
    @ApiOperationSupport(order = 6)
    @PostMapping("/abortMultipart")
    public ResultUtil<String> abortMultipart(@Validated AbortPartRequest request) {
        fileService.abortMultipart(request);
        return ResultUtil.success();
    }

    @Autowired
    private TUploadServiceImpl tUploadService;

//    @ApiOperation("取消分片上传")
//    @ApiOperationSupport(order = 6)
//    @PostMapping("/delete")
//    public ResultUtil<String> delete() {
//        List<TUpload> tUploads = tUploadService.getBaseMapper().selectList(null);
//        for (TUpload tUpload : tUploads) {
//            String objectName = tUpload.getObjectName();
//            try {
//                int i = FastDFSBuilder.build().delete_file1(objectName);
//                System.out.println(i);
//            } catch (IOException | MyException e) {
//                e.printStackTrace();
//            }
//        }
//        return ResultUtil.success("");
//    }
}
