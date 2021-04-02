package com.github.ren.file.controller;

import com.github.ren.file.model.*;
import com.github.ren.file.service.FileServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FileServiceImpl fileService;

    @ApiOperation("检测文件分块情况")
    @GetMapping("/chunk")
    public ResultUtil<CheckChunkResult> checkChunk(CheckChunkRequest checkChunkRequest) {
        return fileService.checkChunk(checkChunkRequest);
    }

    @ApiOperation("上传文件分块")
    @PostMapping("/chunk")
    public ResultUtil<ChunkMergeResult> upload(ChunkRequest chunkRequest, @RequestParam("file") MultipartFile file) {
        return fileService.uploadChunk(chunkRequest, file);
    }

    @ApiOperation("合并文件分块")
    @PostMapping("/merge")
    public ResultUtil<FileUploadResult> merge(ChunkMergeRequest chunkMergeRequest) {
        return fileService.mergeChunk(chunkMergeRequest);
    }
}
