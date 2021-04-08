package com.github.ren.file.service;

import com.github.ren.file.model.ChunkRequest;
import com.github.ren.file.model.ChunkResult;
import com.github.ren.file.model.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Mr Ren
 * @Description: 文件接口
 * @date 2021/4/6 16:23
 */
public interface FileUploadService extends Serializable {

    /**
     * 上传简单小文件
     *
     * @param file
     * @return
     */
    FileUploadResult upload(MultipartFile file);

    /**
     * 检查分片
     *
     * @param chunkRequest
     * @return
     */
    ChunkResult check(ChunkRequest chunkRequest);

    /**
     * 分片上传
     *
     * @param chunkRequest
     * @param file
     * @return
     */
    FileUploadResult upload(ChunkRequest chunkRequest, MultipartFile file);

}
