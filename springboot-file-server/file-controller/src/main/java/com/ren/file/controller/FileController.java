package com.ren.file.controller;

import com.ren.file.dto.request.Chunk;
import com.ren.file.dto.request.MergeChunkDto;
import com.ren.file.dto.response.ChunkRes;
import com.ren.file.dto.response.MergeRes;
import com.ren.file.entity.Fileinfo;
import com.ren.file.service.FileServiceImpl;
import com.ren.file.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return fileService.checkChunk(chunk);
    }

    @PostMapping("/chunk")
    public R<ChunkRes> upload(Chunk chunk, @RequestParam("file") MultipartFile file) {
        return fileService.uploadChunk(chunk, file);
    }

    @PostMapping("/merge")
    public R<Fileinfo> merge(MergeChunkDto mergeChunkDto) {
        return fileService.mergeChunk(mergeChunkDto);
    }
}
