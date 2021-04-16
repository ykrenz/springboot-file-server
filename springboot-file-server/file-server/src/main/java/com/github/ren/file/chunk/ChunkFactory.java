package com.github.ren.file.chunk;

/**
 * @author Mr Ren
 * @Description: 分片工厂
 * @date 2021/4/11 19:40
 */
public interface ChunkFactory {

    /**
     * 分片处理器
     *
     * @return
     */
    ChunkHandler handler();

}
