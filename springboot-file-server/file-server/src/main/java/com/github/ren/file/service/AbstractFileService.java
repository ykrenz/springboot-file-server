//package com.github.ren.file.service;
//
//import com.github.ren.file.model.*;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * @author Mr Ren
// * @Description:
// * @date 2021/4/2 9:38
// */
//public abstract class AbstractFileService implements FileService {
//    FileLock fileLock = new LocalLock();
//
//    @Override
//    public CheckChunkResult checkChunk(CheckChunkRequest checkChunkRequest) {
//        String identifier = checkChunkRequest.getIdentifier();
//        try {
//            fileLock.lock(identifier);
//            return check(checkChunkRequest);
//        } finally {
//            fileLock.unlock(identifier);
//        }
//    }
//
//    @Override
//    public ChunkMergeResult uploadChunk(ChunkRequest chunkRequest, MultipartFile file) {
//        String identifier = chunkRequest.getIdentifier();
//        Integer chunkNumber = chunkRequest.getChunkNumber();
//        try {
//            fileLock.lock(identifier);
//            return upload(chunkRequest, file);
//        } finally {
//            fileLock.unlock(identifier);
//        }
//    }
//
//    @Override
//    public FileUploadResult mergeChunk(ChunkMergeRequest chunkMergeRequest) {
//        String identifier = chunkMergeRequest.getIdentifier();
//        try {
//            fileLock.lock(identifier);
//            return merge(chunkMergeRequest);
//        } finally {
//            fileLock.unlock(identifier);
//        }
//    }
//
//    /**
//     * 检查
//     *
//     * @param checkChunkRequest
//     * @return
//     */
//    public abstract CheckChunkResult check(CheckChunkRequest checkChunkRequest);
//
//    /**
//     * 上传
//     *
//     * @param chunkRequest
//     * @param file
//     * @return
//     */
//    public abstract ChunkMergeResult upload(ChunkRequest chunkRequest, MultipartFile file);
//
//    /**
//     * 合并
//     *
//     * @param chunkMergeRequest
//     * @return
//     */
//    public abstract FileUploadResult merge(ChunkMergeRequest chunkMergeRequest);
//}
//
