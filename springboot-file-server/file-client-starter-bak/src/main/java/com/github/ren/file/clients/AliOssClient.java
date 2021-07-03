package com.github.ren.file.clients;

import com.aliyun.oss.OSS;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;

import java.io.File;

/**
 * 阿里oss存储客户端
 */
public interface AliOssClient extends FileClient {

    /**
     * 获取oss client
     */
    OSS getOssClient();

    /**
     * 下载到本地
     *
     * @param objectName
     * @return
     */
    void downloadFile(String objectName, File file);

    /**
     * 下载整个文件
     *
     * @param objectName
     * @param callback
     * @return
     */
    <T> T downloadFile(String objectName, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     *
     * @param objectName
     * @param fileOffset
     * @param fileSize
     * @param callback
     * @return
     */
    <T> T downloadFile(String objectName, long fileOffset, long fileSize, DownloadCallback<T> callback);

    /**
     * 断点下载
     * objectName
     * downloadFile 下载到本地的文件
     */
    void downloadFileCheckpoint(String objectName, String downloadFile);

    /**
     * 断点下载
     * partSize 分片大小，取值范围为1B~5GB。
     * taskNum 分片下载的并发数
     * checkpointFile 记录本地分片下载结果的文件路径
     */
    void downloadFileCheckpoint(String objectName, String downloadFile, Long partSize, Integer taskNum, String checkpointFile);

    /**
     * 删除文件
     */
    void deleteFile(String yourObjectName);
}
