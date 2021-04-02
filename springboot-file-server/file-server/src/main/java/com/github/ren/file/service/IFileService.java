package com.github.ren.file.service;

import com.github.ren.file.model.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mr Ren
 * @Description:
 * @date 2020/6/2 15:43
 */
public interface IFileService {

    /**
     * 秒传和续传
     */
    ResultUtil<CheckChunkResult> checkChunk(CheckChunkRequest checkChunkRequest);

    /**
     * 上传分片
     */
    ResultUtil<ChunkMergeResult> uploadChunk(ChunkRequest chunkRequest, MultipartFile file);

    /**
     * 合并分片
     */
    ResultUtil<FileUploadResult> mergeChunk(ChunkMergeRequest chunkMergeRequest);


}
