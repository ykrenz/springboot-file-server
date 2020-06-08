package com.ren.file.controller;

import com.ren.file.pojo.request.Chunk;
import com.ren.file.pojo.response.MergeRes;
import com.ren.file.service.FileServiceImpl;
import com.ren.file.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/6/2 15:35
 */
@RestController
@RequestMapping("/api/upload")
public class FileController {
    @Autowired
    private FileServiceImpl fileService;

    @GetMapping("/chunk")
    public R<MergeRes> checkChunk(Chunk chunk) {
        return R.success(fileService.checkChunk(chunk));
    }

    @PostMapping("/chunk")
    public R<String> upload(Chunk chunk) {
        return R.success(fileService.uploadChunk(chunk));
    }

    @PostMapping("/merge")
    public R<String> merge(String identifier, String filename) {
        return R.success(fileService.mergeChunk(identifier, filename));
    }
}
