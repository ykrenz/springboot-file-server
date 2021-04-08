package com.github.ren.file.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: 文件块操作
 * @date 2021/4/6 20:42
 */
public interface ChunkOperator {

    /**
     * 创建分块文件名称
     *
     * @return
     */
    File createChunk();

    /**
     * 上传块到本地
     *
     * @param inputStream
     */
    void upload(InputStream inputStream) throws IOException;

    /**
     * 删除当前分片
     *
     * @return
     */
    boolean delCurrentChunk();

    /**
     * 删除所有分片
     *
     * @return
     */
    boolean delAllChunk();

    /**
     * 是否需要合并
     *
     * @param totalSize 文件总大小
     * @return
     */
    boolean needMerge(long totalSize);

    /**
     * 获取已经上传的块索引
     *
     * @return
     */
    List<Long> getChunkNumbers();

    /**
     * 获取所有分块数据
     *
     * @return
     */
    List<File> getChunks();

    /**
     * 是否已经上传
     *
     * @param currentChunkSize 当前块实际大小
     * @return
     */
    boolean isUpload(long currentChunkSize);

}
