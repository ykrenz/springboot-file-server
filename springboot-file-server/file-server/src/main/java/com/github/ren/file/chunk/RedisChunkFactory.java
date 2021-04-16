package com.github.ren.file.chunk;

/**
 * @author Mr Ren
 * @Description: redis分片工厂
 * @date 2021/4/11 19:45
 */
public class RedisChunkFactory implements ChunkFactory {

    @Override
    public ChunkHandler handler() {
        return new RedisChunkHandler();
    }
}
