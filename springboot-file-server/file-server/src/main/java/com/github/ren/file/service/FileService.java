package com.github.ren.file.service;

import com.github.ren.file.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/4/2 11:38
 */
public interface FileService extends Serializable {
    /**
     * 秒传和续传
     */
    CheckChunkResult checkChunk(CheckChunkRequest checkChunkRequest);

    /**
     * 上传分片
     */
    ChunkMergeResult uploadChunk(ChunkRequest chunkRequest, MultipartFile file);

    /**
     * 合并分片
     */
    FileUploadResult mergeChunk(ChunkMergeRequest chunkMergeRequest);
}
