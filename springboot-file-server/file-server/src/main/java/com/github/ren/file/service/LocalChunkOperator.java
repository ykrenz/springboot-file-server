package com.github.ren.file.service;

import com.github.ren.file.ex.ChunkException;
import com.github.ren.file.model.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RenYinKui
 * @Description: 本地块文件操作类
 * @date 2021/4/7 10:42
 */
@Slf4j
@Getter
public class LocalChunkOperator implements ChunkOperator {

    /**
     * 临时块文件目录
     */
    private final String chunkTempDir;
    /**
     * 文件md5
     */
    private final String md5;
    /**
     * 块索引
     */
    private final Long chunkNumber;
    /**
     * 块大小
     */
    private final Long chunkSize;

    private final static String UNDERLINE = "_";

    public LocalChunkOperator(String chunkTempDir,
                              String md5,
                              Long chunkNumber,
                              Long chunkSize) {
        this.chunkTempDir = chunkTempDir;
        this.md5 = md5;
        this.chunkNumber = chunkNumber;
        this.chunkSize = chunkSize;
    }

    @Override
    public File createChunk() {
        String fileName = md5 + UNDERLINE + chunkNumber;
        return new File(chunkTempDir, fileName);
    }

    @Override
    public void upload(InputStream inputStream) throws IOException {
        File chunkFile = createChunk();
        FileUtils.copyInputStreamToFile(inputStream, chunkFile);
    }

    @Override
    public boolean delCurrentChunk() {
        File chunkFile = createChunk();
        if (chunkFile.exists()) {
            return FileUtils.deleteQuietly(chunkFile);
        }
        return true;
    }

    @Override
    public boolean delAllChunk() {
        List<File> chunks = getChunks();
        chunks.forEach(FileUtils::deleteQuietly);
        return getChunks().isEmpty();
    }

    @Override
    public boolean needMerge(long totalSize) {
        //查询文件总大小
        long fileSize = getChunks().stream().mapToLong(File::length).sum();
        return totalSize == fileSize;
    }

    @Override
    public List<Long> getChunkNumbers() {
        return getChunks()
                .stream()
                .map(f -> Long.parseLong(StringUtils.substringAfter(f.getName(), UNDERLINE)))
                .sorted().collect(Collectors.toList());
    }

    @Override
    public List<File> getChunks() {
        File chunkFolder = new File(chunkTempDir);
        if (!chunkFolder.exists() && !chunkFolder.mkdirs()) {
            log.error("创建临时文件夹失败chunkTempDir={}", chunkTempDir);
            throw new ChunkException(ErrorCode.SERVER_ERROR);
        }
        //分块列表
        File[] fileArray = chunkFolder.listFiles(f -> f.getName().startsWith(md5));
        if (fileArray == null || fileArray.length == 0) {
            return new ArrayList<>(0);
        }
        List<File> files = Arrays.asList(fileArray);
        files.sort(Comparator.comparingLong(
                f -> Long.parseLong(StringUtils.substringAfter(f.getName(), UNDERLINE))));
        return files;
    }

    @Override
    public boolean isUpload(long currentChunkSize) {
        File file = createChunk();
        return file.exists() && chunkSize == currentChunkSize;
    }

}
