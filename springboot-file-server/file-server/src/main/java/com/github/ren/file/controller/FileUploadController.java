package com.github.ren.file.controller;

import com.github.ren.file.model.request.CheckChunkRequest;
import com.github.ren.file.model.request.ChunkMergeRequest;
import com.github.ren.file.model.request.ChunkRequest;
import com.github.ren.file.model.result.CheckFileResult;
import com.github.ren.file.model.result.ChunkUploadResult;
import com.github.ren.file.model.result.FileUploadResult;
import com.github.ren.file.model.result.ResultUtil;
import com.github.ren.file.service.impl.FileUploadServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件服务接口
 * @date 2020/6/2 15:35
 */
@RestController
@RequestMapping("upload")
@Api(tags = "文件")
public class FileUploadController {

    @Autowired
    @Qualifier("SimpleUploadServiceImpl")
    private FileUploadServiceImpl simpleUploadService;

    @ApiOperation("简单文件上传")
    @PostMapping(value = "/simple")
    public ResultUtil<FileUploadResult> upload(@RequestPart("file") MultipartFile file) {
        return ResultUtil.success(simpleUploadService.upload(file));
    }

    @ApiOperation("检测文件分块情况")
    @GetMapping("/chunk")
    public ResultUtil<CheckFileResult> checkChunk(@Validated CheckChunkRequest request) {
        return ResultUtil.success(simpleUploadService.check(request));
    }

    @ApiOperation("上传文件分块")
    @PostMapping("/chunk")
    public ResultUtil<ChunkUploadResult> uploadChunk(@Validated ChunkRequest request, @RequestPart("file") MultipartFile file) {
        return ResultUtil.success(simpleUploadService.uploadChunk(request, file));
    }

    @ApiOperation("合并分块")
    @PostMapping("/merge")
    public ResultUtil<FileUploadResult> merge(@Validated ChunkMergeRequest request) {
        return ResultUtil.success(simpleUploadService.merge(request));
    }
}
