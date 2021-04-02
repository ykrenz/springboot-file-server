package com.github.ren.file.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.clients.FileClient;
import com.github.ren.file.config.FileServerProperties;
import com.github.ren.file.entity.Fileinfo;
import com.github.ren.file.mapper.FileinfoMapper;
import com.github.ren.file.model.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @Description:
 * @date 2020/6/2 16:16
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Autowired
    private FileClient fileClient;
    @Autowired
    private FileServerProperties fileServerProperties;
    @Resource
    private FileinfoMapper fileinfoMapper;

    @Override
    public ResultUtil<CheckChunkResult> checkChunk(CheckChunkRequest checkChunkRequest) {
        String identifier = checkChunkRequest.getIdentifier();
        Integer totalChunks = checkChunkRequest.getTotalChunks();
        CheckChunkResult checkChunkResult = new CheckChunkResult();
        //查询数据库MD5校验文件是否已经上传,实现秒传
        Fileinfo fileinfo = fileinfoMapper.selectOne(Wrappers.<Fileinfo>lambdaQuery().eq(Fileinfo::getMd5, identifier));
        checkChunkResult.setUploaded(fileinfo != null);
        List<File> mergeFileList = this.getMergeFileList(identifier);
        if (fileinfo != null) {
            fileinfo.setPath(fileClient.getAccessPath(fileinfo.getPath()));
            checkChunkResult.setFileinfo(fileinfo);
            //删除可能存在的临时文件
            checkChunkResult.setMerge(false);
            mergeFileList.forEach(FileUtils::deleteQuietly);
            checkChunkResult.setChunkNumbers(Lists.newArrayListWithCapacity(0));
        } else {
            List<Integer> chunkNumbers = this.getMergeChunkNumbers(identifier);
            checkChunkResult.setChunkNumbers(chunkNumbers);
            checkChunkResult.setMerge(totalChunks == chunkNumbers.size());
        }
        return ResultUtil.success(checkChunkResult);
    }

    @Override
    public ResultUtil<ChunkMergeResult> uploadChunk(ChunkRequest chunkRequest, MultipartFile file) {
        //直接上传到文件服务
        if (chunkRequest.getTotalChunks() <= 1) {
            try (InputStream inputStream = file.getInputStream()) {
                String path = fileClient.uploadFile(inputStream,
                        chunkRequest.getIdentifier().concat(".") + FilenameUtils.getExtension(chunkRequest.getFilename()));
                Fileinfo fileinfo = Fileinfo.builder()
                        .md5(chunkRequest.getIdentifier())
                        .filename(chunkRequest.getFilename())
                        .filesize(chunkRequest.getTotalSize())
                        .extension(chunkRequest.getExtension())
                        .filetype(chunkRequest.getFileType())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .path(path)
                        .build();
                fileinfoMapper.insert(fileinfo);
                fileinfo.setPath(fileClient.getAccessPath(path));
                ChunkMergeResult chunkMergeResult = ChunkMergeResult.builder().
                        merge(false)
                        .fileinfo(fileinfo)
                        .build();
                return ResultUtil.success(chunkMergeResult);
            } catch (IOException e) {
                return ResultUtil.error(ErrorCode.UPLOAD_CHUNK_ERROR);
            }
        } else {
            //上传到分块目录
            Integer chunkNumber = chunkRequest.getChunkNumber();
            String identifier = chunkRequest.getIdentifier();
            String chunkTempPath = fileServerProperties.getChunkTempPath();
            String chunkName = identifier.concat("-") + chunkNumber;
            File chunkFile = new File(chunkTempPath, chunkName);
            Lock lock = FileLock.getLock(chunkName);
            try {
                lock.lock();
                if (chunkFile.exists() && chunkFile.length() == chunkRequest.getCurrentChunkSize()) {
                    log.info("该分块已经上传{}", chunkFile.getName());
                } else {
                    FileUtils.copyInputStreamToFile(file.getInputStream(), chunkFile);
                }
                //TODO 多线程情况下 merge判断不能根据块 要根据总大小才准确
                ChunkMergeResult chunkMergeResult = ChunkMergeResult.builder().
                        merge(chunkRequest.getChunkNumber().equals(chunkRequest.getTotalChunks()))
                        .build();
                return ResultUtil.success(chunkMergeResult);
            } catch (Exception e) {
                log.error("上传异常", e);
                return ResultUtil.error(ErrorCode.UPLOAD_CHUNK_ERROR);
            } finally {
                //解锁
                lock.unlock();
                FileLock.removeLock(chunkName);
            }
        }
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    private String getChunkTempPath() {
        return fileServerProperties.getChunkTempPath();
    }

    /**
     * 获取分片文件 并排序
     *
     * @param identifier
     * @return
     */
    private List<File> getMergeFileList(String identifier) {
        return fileClient.getMergeFileList(this.getChunkTempPath(), chunk1 -> chunk1.getName().startsWith(identifier),
                Comparator.comparing(f -> Integer.parseInt(f.getName().substring(f.getName().lastIndexOf("-") + 1))));
    }

    /**
     * 获取已经上传分片集合 断点续传
     *
     * @param identifier
     * @return
     */
    private List<Integer> getMergeChunkNumbers(String identifier) {
        return this.getMergeFileList(identifier).stream()
                .map(f -> Integer.parseInt(f.getName().substring(f.getName().lastIndexOf("-") + 1)))
                .sorted(Comparator.comparing(Integer::new))
                .collect(Collectors.toList());
    }

    @Override
    public ResultUtil<FileUploadResult> mergeChunk(ChunkMergeRequest chunkMergeRequest) {
        String identifier = chunkMergeRequest.getIdentifier();
        String filename = chunkMergeRequest.getFilename();
        Long filesize = chunkMergeRequest.getFilesize();
        Lock lock = FileLock.getLock(identifier);
        try {
            lock.lock();
            List<File> mergeFileList = this.getMergeFileList(identifier);
            if (mergeFileList.isEmpty()) {
                //线程并发 优先获取到锁的线程已经上传完该文件，查询数据库，进行返回
                Fileinfo fileinfo = fileinfoMapper.selectOne(Wrappers.<Fileinfo>lambdaQuery().eq(Fileinfo::getMd5, identifier));
                if (fileinfo == null) {
                    return ResultUtil.error(ErrorCode.UPLOAD_MERGE_ERROR);
                }
                FileUploadResult fileUploadResult = FileUploadResult.builder().filename(filename)
                        .filesize(filesize)
                        .md5(identifier)
                        .path(fileClient.getAccessPath(fileinfo.getPath())).build();
                return ResultUtil.success(fileUploadResult);
            }
            String path = fileClient.uploadPart(mergeFileList, filename);
            mergeFileList.forEach(FileUtils::deleteQuietly);
            Fileinfo fileinfo = Fileinfo.builder()
                    .md5(identifier)
                    .filename(filename)
                    .filesize(filesize)
                    .extension(chunkMergeRequest.getExtension())
                    .filetype(chunkMergeRequest.getFileType())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .path(path)
                    .build();
            fileinfoMapper.insert(fileinfo);

            FileUploadResult fileUploadResult = FileUploadResult.builder().filename(filename)
                    .filesize(filesize)
                    .md5(identifier)
                    .path(fileClient.getAccessPath(fileinfo.getPath())).build();
            return ResultUtil.success(fileUploadResult);
        } catch (Exception e) {
            log.error("合并分片失败", e);
            return ResultUtil.error(ErrorCode.UPLOAD_MERGE_ERROR);
        } finally {
            //解锁
            lock.unlock();
            FileLock.removeLock(identifier);
        }
    }
}
