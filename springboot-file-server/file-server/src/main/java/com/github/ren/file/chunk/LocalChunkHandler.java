package com.github.ren.file.chunk;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: 本地分片处理器
 * @date 2021/4/11 0:09
 */
public class LocalChunkHandler implements ChunkHandler {
    /**
     * 分块文件存放目录
     */
    private String chunkDir;

    public LocalChunkHandler(String chunkDir) {
        this.chunkDir = chunkDir;
    }

    /**
     * 检测文件夹
     *
     * @param chunkDir
     */
    private void checkDir(String chunkDir) {
        File dir = new File(chunkDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("创建文件夹失败" + chunkDir);
        }
        if (dir.exists() && !dir.isDirectory()) {
            throw new IllegalStateException("创建文件夹失败" + chunkDir);
        }
    }

    private String genKeyByMd5AndNumber(String md5, long number) {
        return md5 + StrUtil.UNDERLINE + number;
    }

    private int getNumberByKey(String key) {
        return Integer.parseInt(StringUtils.substringAfter(key, StrUtil.UNDERLINE));
    }

    private File getChunkFile(String key) {
        checkDir(chunkDir);
        return new File(chunkDir, key);
    }

    @Override
    public Chunk create(String md5, int number, long size) {
        return new ChunkInfo(genKeyByMd5AndNumber(md5, number), number, size);
    }

    @Override
    public boolean isUpload(Chunk chunk) {
        File chunkFile = getChunkFile(chunk.getKey());
        return chunkFile.exists() && chunkFile.length() == chunk.getSize();
    }

    @Override
    public List<Chunk> getChunks(String key) {
        checkDir(chunkDir);
        File chunkFolder = new File(chunkDir);
        //分块列表
        File[] fileArray = chunkFolder.listFiles(f -> f.getName().startsWith(key));
        if (fileArray == null || fileArray.length == 0) {
            return new ArrayList<>(0);
        }
        List<Chunk> list = new ArrayList<>(fileArray.length);
        for (File file : fileArray) {
            Chunk localChunk = new ChunkInfo(
                    file.getName(),
                    getNumberByKey(file.getName()),
                    file.length());
            list.add(localChunk);
        }
        return list;
    }

    @Override
    public long getTotalSize(String md5) {
        return getChunks(md5).stream().mapToLong(Chunk::getSize).sum();
    }

    @Override
    public int getTotalNumber(String md5) {
        return getChunks(md5).size();
    }

    @Override
    public boolean deleteChunk(Chunk chunk) {
        File file = getChunkFile(chunk.getKey());
        return file.exists() && file.delete();
    }

    @Override
    public boolean deleteChunk(String key) {
        List<Chunk> chunks = getChunks(key);
        boolean result = true;
        for (Chunk chunk : chunks) {
            if (!deleteChunk(chunk)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public void upload(Chunk chunk, InputStream is) throws IOException {
        File file = getChunkFile(chunk.getKey());
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            is.close();
        }
    }

    @Override
    public InputStream getInputStream(Chunk chunk) throws IOException {
        File file = getChunkFile(chunk.getKey());
        return new FileInputStream(file);
    }
}
