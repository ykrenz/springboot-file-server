package com.github.ren.file.chunk;

/**
 * @author Mr Ren
 * @Description: 本地分片工厂
 * @date 2021/4/11 19:45
 */
public class LocalChunkFactory implements ChunkFactory {

    private String chunkDir;

    public LocalChunkFactory(String chunkDir) {
        this.chunkDir = chunkDir;
    }

    @Override
    public ChunkHandler handler() {
        return new LocalChunkHandler(chunkDir);
    }
}
