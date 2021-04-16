package com.github.ren.file.chunk;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mr Ren
 * @Description: 分片上传器
 * @date 2021/4/10 23:53
 */
public interface ChunkUploader {
    /**
     * 上传分片
     *
     * @param chunk
     * @param is
     * @throws IOException
     */
    void upload(Chunk chunk, InputStream is) throws IOException;

    /**
     * 获取分片流信息
     *
     * @param chunk
     * @return
     * @throws IOException
     */
    InputStream getInputStream(Chunk chunk) throws IOException;

}

