package com.github.ren.file.controller;

import com.github.ren.file.ex.UploadException;
import com.github.ren.file.model.*;
import com.github.ren.file.service.impl.FileUploadServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件服务接口
 * @date 2020/6/2 15:35
 */
@RestController
@RequestMapping("/api/upload")
@Api(tags = "文件")
public class FileController {

    @Autowired
    @Qualifier("FileUploadServiceImpl")
    private FileUploadServiceImpl fileUploadService;

    @ApiOperation("简单文件上传")
    @PostMapping(value = "/simple")
    public ResultUtil<FileUploadResult> upload(@RequestPart("file") MultipartFile file) {
        long size = file.getSize();
        int large = 5242880;
        if (size > large) {
            throw new UploadException(ErrorCode.FILE_TO_LARGE);
        }
        return ResultUtil.success(fileUploadService.upload(file));
    }

    @ApiOperation("检测文件分块情况")
    @GetMapping("/chunk")
    public ResultUtil<ChunkResult> checkChunk(ChunkRequest chunkRequest) {
        return ResultUtil.success(fileUploadService.check(chunkRequest));
    }

    @ApiOperation("上传文件分块")
    @PostMapping("/chunk")
    public ResultUtil<FileUploadResult> uploadChunk(ChunkRequest chunkRequest, @RequestPart("file") MultipartFile file) {
        return ResultUtil.success(fileUploadService.upload(chunkRequest, file));
    }
}
