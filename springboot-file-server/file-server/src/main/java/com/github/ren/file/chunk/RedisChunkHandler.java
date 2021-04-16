package com.github.ren.file.chunk;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: redis 分片处理器
 * @date 2021/4/11 0:02
 */
public class RedisChunkHandler implements ChunkHandler {

    @Override
    public void upload(Chunk chunk, InputStream is) throws IOException {

    }

    @Override
    public InputStream getInputStream(Chunk chunk) {
        return null;
    }

    @Override
    public Chunk create(String md5, int number, long size) {
        return null;
    }

    @Override
    public boolean isUpload(Chunk chunk) {
        return false;
    }

    @Override
    public List<Chunk> getChunks(String key) {
        return null;
    }

    @Override
    public long getTotalSize(String md5) {
        return 0;
    }

    @Override
    public int getTotalNumber(String md5) {
        return 0;
    }

    @Override
    public boolean deleteChunk(Chunk chunk) {
        return false;
    }

    @Override
    public boolean deleteChunk(String key) {
        return false;
    }

}
