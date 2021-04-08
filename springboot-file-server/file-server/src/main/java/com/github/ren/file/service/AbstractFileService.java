package com.github.ren.file.service;

import com.github.ren.file.model.ChunkRequest;
import com.github.ren.file.model.ChunkResult;
import com.github.ren.file.model.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mr Ren
 * @Description: 抽象类
 * @date 2021/4/2 9:38
 */
public abstract class AbstractFileService implements FileUploadService {

    private FileLocks<String> fileLock = new SegmentLock<>();

    @Override
    public ChunkResult check(ChunkRequest chunkRequest) {
        String identifier = chunkRequest.getIdentifier();
        try {
            fileLock.lock(identifier);
            return checkChunk(chunkRequest);
        } finally {
            fileLock.unlock(identifier);
        }
    }

    @Override
    public FileUploadResult upload(ChunkRequest chunkRequest, MultipartFile multipartFile) {
        //检测文件是否已经上传 上传过则秒传
        ChunkResult chunkResult = check(chunkRequest);
        if (chunkResult.getFile() != null) {
            return chunkResult.getFile();
        }
        //检测文件分片大小 总大小和分块大小一致 说明只有一个分片 直接用小文件上传
        long size = multipartFile.getSize();
        if (size == chunkRequest.getTotalSize()) {
            return upload(multipartFile);
        }

        //上传分片操作 加锁 lock为md5值和分块索引 保证多线程同时传一个文件分片是完整的
        String lock = chunkRequest.getIdentifier() + "_" + chunkRequest.getChunkNumber();
        try {
            fileLock.lock(lock);
            return uploadChunk(chunkRequest, multipartFile);
        } finally {
            fileLock.unlock(lock);
        }
    }

    /**
     * 检查
     *
     * @param chunkRequest
     * @return
     */
    protected abstract ChunkResult checkChunk(ChunkRequest chunkRequest);

    /**
     * 上传
     *
     * @param chunkRequest
     * @param multipartFile
     * @return
     */
    protected abstract FileUploadResult uploadChunk(ChunkRequest chunkRequest, MultipartFile multipartFile);

    /**
     * 合并
     *
     * @param chunkRequest
     * @return
     */
    protected abstract FileUploadResult mergeChunk(ChunkRequest chunkRequest);

}

