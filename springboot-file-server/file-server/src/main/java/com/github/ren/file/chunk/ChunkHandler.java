package com.github.ren.file.chunk;

import java.util.List;

/**
 * @author Mr Ren
 * @Description: 分片处理器
 * @date 2021/4/10 12:12
 */
public interface ChunkHandler extends ChunkUploader {

    /**
     * 创建分片
     *
     * @param md5    文件md5
     * @param number 分片索引
     * @param size   分片大小
     * @return
     */
    Chunk create(String md5, int number, long size);

    /**
     * 判断分块是否已经存在
     *
     * @param chunk 分块文件
     * @return
     */
    boolean isUpload(Chunk chunk);

    /**
     * 获取所有分片
     *
     * @param key
     * @return
     */
    List<Chunk> getChunks(String key);

    /**
     * 获取分片总大小
     *
     * @param md5 文件md5
     * @return
     */
    long getTotalSize(String md5);

    /**
     * 获取分片总数
     *
     * @param md5 文件md5
     * @return
     */
    int getTotalNumber(String md5);

    /**
     * 删除当前分片
     *
     * @param chunk
     * @return
     */
    boolean deleteChunk(Chunk chunk);

    /**
     * 删除所有分片
     *
     * @param key
     * @return
     */
    boolean deleteChunk(String key);

//    /**
//     * 关闭文件流信息
//     *
//     * @param key
//     */
//    void close(String key);
}
