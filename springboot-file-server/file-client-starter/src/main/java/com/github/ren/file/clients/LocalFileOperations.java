package com.github.ren.file.clients;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

/**
 * 本地文件操作类
 */
public interface LocalFileOperations {
    /**
     * 拷贝本地文件
     *
     * @param inputFile
     * @param outFile
     */
    void copyFile(File inputFile, File outFile) throws IOException;

    /**
     * 拷贝流到本地
     *
     * @param inputStream
     * @param outFile
     */
    void copyFile(InputStream inputStream, File outFile) throws IOException;

    /**
     * 拆分文件
     *
     * @param file
     * @param chunkPath
     * @param chunkSize
     */
    void chunkFile(File file, String chunkPath, long chunkSize) throws IOException;

    /**
     * 合并文件
     *
     * @param chunkPath
     * @param mergeFile
     * @return
     */
    String mergeFile(String chunkPath, File mergeFile) throws IOException;

    /**
     * 获取合并分片文件数据
     *
     * @param chunkPath
     * @return
     */
    List<File> getMergeFileListByNumber(String chunkPath);

    /**
     * 获取合并分片文件数据
     *
     * @param chunkPath
     * @return
     */
    List<File> getMergeFileListByName(String chunkPath);

    /**
     * 获取合并分片文件数据
     *
     * @param chunkPath
     * @param comparator 文件排序
     * @return
     */
    List<File> getMergeFileList(String chunkPath, Comparator<File> comparator);

    /**
     * 获取合并分片文件数据
     *
     * @param chunkPath
     * @param comparator 文件排序
     * @return
     */
    List<File> getMergeFileList(String chunkPath, FileFilter fileFilter, Comparator<File> comparator);

    /**
     * 获取content type
     *
     * @param file
     * @return
     */
    String getContentType(File file);
}
