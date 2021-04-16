package com.github.ren.file.service;

import com.github.ren.file.chunk.ChunkHandler;
import com.github.ren.file.chunk.LocalChunkFactory;
import com.github.ren.file.chunk.RedisChunkFactory;
import com.github.ren.file.config.FileServerProperties;
import com.github.ren.file.model.ChunkType;
import com.github.ren.file.lock.FileLock;
import com.github.ren.file.lock.RedisLock;
import com.github.ren.file.lock.SegmentLock;
import org.springframework.stereotype.Component;

/**
 * @author Mr Ren
 * @Description: 文件帮助类
 * @date 2021/4/12 11:56
 */
@Component
public class FileServerHelper {

    private ChunkHandler chunkHandler;

    private FileLock fileLock;

    private FileServerProperties fileServerProperties;

    public FileServerHelper(FileServerProperties fileServerProperties) {
        this.fileServerProperties = fileServerProperties;
        String chunkTempPath = fileServerProperties.getChunkTempPath();
        ChunkType chunk = fileServerProperties.getChunk();
        switch (chunk) {
            case REDIS:
                fileLock = new RedisLock();
                chunkHandler = new RedisChunkFactory().handler();
                break;
            case LOCAL:
            default:
                fileLock = new SegmentLock();
                chunkHandler = new LocalChunkFactory(chunkTempPath).handler();
        }

    }

    public FileServerProperties getFileServerProperties() {
        return fileServerProperties;
    }

    public ChunkHandler getChunkHandler() {
        return chunkHandler;
    }

    public FileLock getFileLock() {
        return fileLock;
    }

}
