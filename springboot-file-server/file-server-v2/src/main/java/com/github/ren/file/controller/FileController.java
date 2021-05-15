package com.github.ren.file.controller;

import com.github.ren.file.model.ResultUtil;
import com.github.ren.file.model.request.PartRequest;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResultUtil<String> upload(MultipartFile file) {
        return ResultUtil.success(fileService.upload(file));
    }

    @PostMapping("/check")
    public ResultUtil<String> check(@RequestParam("md5") String md5) {
        return ResultUtil.success(fileService.check(md5));
    }

    @PostMapping("/initiateMultipartUpload")
    public ResultUtil<String> initiateMultipartUpload(@RequestParam("filename") String filename) {
        return ResultUtil.success(fileService.initiateMultipartUpload(filename));
    }

    @ApiOperation("获取分片列表 断点续传")
    @PostMapping("/listParts")
    public List<PartInfo> listParts(String uploadId) {
        //TODO 查询数据库 uploadId
        return null;
    }

    @ApiOperation("上传文件分片")
    @PostMapping("/uploadPart")
    public ResultUtil uploadPart(@Validated PartRequest partRequest) {
        return ResultUtil.success(fileService.uploadPart(partRequest));
    }

    @ApiOperation("合并文件分片")
    @PostMapping("/completeMultipartUpload")
    public CompleteMultipart completeMultipartUpload(String uploadId, String md5) {
        //TODO 查询数据库 uploadId
        String objectName = null;
        //TODO 保存文件 并 删除分片记录
        return null;
    }

    @ApiOperation("取消上传")
    @PostMapping("/abortMultipartUpload")
    public String abortMultipartUpload(String uploadId) {
        //TODO 查询数据库 uploadId
        String objectName = null;
//        fileClient.abortMultipartUpload(uploadId, objectName);
        return "success";
    }
}
