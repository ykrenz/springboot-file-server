package com.github.ren.file.service;

import com.github.ren.file.model.request.CheckChunkRequest;
import com.github.ren.file.model.request.ChunkMergeRequest;
import com.github.ren.file.model.request.ChunkRequest;
import com.github.ren.file.model.result.CheckFileResult;
import com.github.ren.file.model.result.ChunkUploadResult;
import com.github.ren.file.model.result.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mr Ren
 * @Description: upload接口
 * @date 2021/4/11 0:25
 */
public interface FileUploadService {
    /**
     * 上传简单小文件
     *
     * @param file
     * @return
     */
    FileUploadResult upload(MultipartFile file);

    /**
     * 检测分片结果 秒传
     *
     * @param request
     * @return
     */
    CheckFileResult check(CheckChunkRequest request);

    /**
     * 上传分片
     *
     * @param request
     * @param file
     * @return
     */
    ChunkUploadResult uploadChunk(ChunkRequest request, MultipartFile file);

    /**
     * 合并分片
     *
     * @param request
     * @return
     */
    FileUploadResult merge(ChunkMergeRequest request);
}
