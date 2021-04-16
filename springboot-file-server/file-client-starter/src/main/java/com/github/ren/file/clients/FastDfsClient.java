package com.github.ren.file.clients;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * fastdfs存储客户端
 */
public interface FastDfsClient extends FileClient {

    /**
     * 上传file返回StorePath
     */
    StorePath uploadFileStorePath(File file, String yourObjectName);

    /**
     * 上传is返回StorePath
     */
    StorePath uploadFileStorePath(InputStream is, String yourObjectName);

    /**
     * 上传byte[]返回StorePath
     */
    StorePath uploadFileStorePath(byte[] content, String yourObjectName);

    /**
     * 上传url返回StorePath
     */
    StorePath uploadFileStorePath(String url, String yourObjectName);

    /**
     * 分片上传file返回StorePath
     */
    StorePath uploadPartStorePath(List<UploadPart> parts, String yourObjectName);

    /**
     * file生成缩略图
     */
    StorePath uploadImageAndCrtThumbImage(File file, String yourObjectName);

    /**
     * InputStream生成缩略图
     */
    StorePath uploadImageAndCrtThumbImage(InputStream is, String yourObjectName);

    /**
     * 下载整个文件
     *
     * @param groupName
     * @param path
     * @param callback
     * @return
     */
    <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     *
     * @param groupName
     * @param path
     * @param fileOffset
     * @param fileSize
     * @param callback
     * @return
     */
    <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback);

    /**
     * 删除文件
     *
     * @param groupName
     * @param path
     */
    void deleteFile(String groupName, String path);

    /**
     * 删除文件
     */
    void deleteFile(StorePath storePath);

    /**
     * getFullPath
     */
    String getFullPath(StorePath storePath);

    /**
     * getPath
     */
    String getPath(StorePath storePath);

    /**
     * getGroupPath
     */
    String getGroup(StorePath storePath);

}
