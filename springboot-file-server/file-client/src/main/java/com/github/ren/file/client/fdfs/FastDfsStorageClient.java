package com.github.ren.file.client.fdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.File;
import java.io.InputStream;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public interface FastDfsStorageClient {

    /**
     * 获取TrackerClient
     *
     * @return
     */
    TrackerClient getTrackerClient();

    /**
     * 获取TrackerServer
     *
     * @return StorageServer信息
     */
    TrackerServer getTrackerServer();

    /**
     * 获取StorageServer
     *
     * @return StorageServer信息
     */
    StorageServer getStorageServer();

    /**
     * 获取当前client
     *
     * @return StorageClient1 fastdfs官方提供的client
     */
    StorageClient1 getStorageClient();

    /**
     * 上传文件(文件不可修改)
     * <p>
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param file
     * @return 上传后的文件路径
     */
    String uploadFile(File file);

    /**
     * 上传文件(文件不可修改)
     * <p>
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param file
     * @param metaData
     * @return 上传后的文件路径
     */
    String uploadFile(File file, NameValuePair[] metaData);


    /**
     * 上传文件(文件不可修改)
     * <p>
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param groupName
     * @param file
     * @return 上传后的文件路径
     */
    String uploadFile(String groupName, File file);

    /**
     * 上传文件(文件不可修改)
     * <p>
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param groupName
     * @param file
     * @param metaData
     * @return 上传后的文件路径
     */
    String uploadFile(String groupName, File file, NameValuePair[] metaData);

    /**
     * 上传一般文件
     *
     * @param inputStream
     * @param fileExtName
     * @return
     */
    String uploadFile(InputStream inputStream, String fileExtName);

    /**
     * 上传一般文件
     *
     * @param inputStream
     * @param fileExtName
     * @param metaData
     * @return
     */
    String uploadFile(InputStream inputStream, String fileExtName, NameValuePair[] metaData);

    /**
     * 上传文件(文件不可修改)
     * <p>
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param groupName
     * @param inputStream
     * @param fileExtName
     * @return
     */
    String uploadFile(String groupName, InputStream inputStream, String fileExtName);

    /**
     * 上传文件(文件不可修改)
     * <p>
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param groupName
     * @param inputStream
     * @param fileExtName
     * @return
     */
    String uploadFile(String groupName, InputStream inputStream, String fileExtName, NameValuePair[] metaData);


    /**
     * 上传从文件
     *
     * @param masterFilename 主文件路径
     * @param prefixName     从文件前缀
     * @param inputStream
     * @param fileExtName
     * @return
     */
    String uploadFile(String masterFilename, String prefixName, InputStream inputStream, String fileExtName);

    /**
     * 上传从文件
     *
     * @param masterFilename 主文件路径
     * @param prefixName     从文件前缀
     * @param inputStream
     * @param fileExtName
     * @param metaData
     * @return
     */
    String uploadFile(String masterFilename, String prefixName, InputStream inputStream, String fileExtName, NameValuePair[] metaData);

    /**
     * 删除文件
     *
     * @param filePath 文件路径(groupName/path)
     * @return
     */
    boolean deleteFile(String filePath);

    /**
     * 删除文件
     *
     * @param groupName
     * @param path
     * @return
     */
    boolean deleteFile(String groupName, String path);


    /**
     * 获取文件元信息
     *
     * @param filePath 文件路径(groupName/path)
     * @return
     */
    NameValuePair[] getMetadata(String filePath);

    /**
     * 获取文件元信息
     *
     * @param groupName
     * @param path
     * @return
     */
    NameValuePair[] getMetadata(String groupName, String path);

    /**
     * 修改文件元信息（覆盖）
     *
     * @param filePath 文件路径(groupName/path)
     * @param metaData
     */
    boolean overwriteMetadata(String filePath, NameValuePair[] metaData);

    /**
     * 修改文件元信息（覆盖）
     *
     * @param groupName
     * @param path
     * @param metaData
     */
    boolean overwriteMetadata(String groupName, String path, NameValuePair[] metaData);


    /**
     * 修改文件元信息（合并）
     *
     * @param filePath 文件路径(groupName/path)
     * @param metaData
     */
    boolean mergeMetadata(String filePath, NameValuePair[] metaData);

    /**
     * 修改文件元信息（合并）
     *
     * @param groupName
     * @param path
     * @param metaData
     */
    boolean mergeMetadata(String groupName, String path, NameValuePair[] metaData);

    /**
     * 查看文件的信息
     *
     * @param filePath 文件路径(groupName/path)
     * @return
     */
    FileInfo queryFileInfo(String filePath);

    /**
     * 查看文件的信息
     *
     * @param groupName
     * @param path
     * @return
     */
    FileInfo queryFileInfo(String groupName, String path);

    /**
     * 下载整个文件到本地文件
     *
     * @param filePath  文件路径(groupName/path)
     * @param localFile 本地文件
     * @return
     */
    boolean downloadLocalFile(String filePath, String localFile);

    /**
     * 下载整个文件到本地文件
     *
     * @param groupName
     * @param path
     * @param localFile 本地文件
     * @return
     */
    boolean downloadLocalFile(String groupName, String path, String localFile);

    /**
     * 下载文件片段到本地文件
     *
     * @param groupName
     * @param path
     * @param localFile 本地文件
     * @return
     */
    boolean downloadLocalFile(String groupName, String path, long offset, long size, String localFile);

    /**
     * 下载整个文件到字节数组
     *
     * @param filePath 文件路径(groupName/path)
     * @return
     */
    byte[] downloadFile(String filePath);

    /**
     * 下载文件片段到字节数组
     *
     * @param filePath 文件路径(groupName/path)
     * @param offset
     * @param size
     * @return
     */
    byte[] downloadFile(String filePath, long offset, long size);

    /**
     * 下载整个文件到字节数组
     *
     * @param groupName
     * @param path
     * @return
     */
    byte[] downloadFile(String groupName, String path);

    /**
     * 下载文件片段到字节数组
     *
     * @param groupName
     * @param path
     * @param offset
     * @param size
     * @return
     */
    byte[] downloadFile(String groupName, String path, long offset, long size);

    /**
     * 下载整个文件到DownloadCallback
     *
     * @param filePath 文件路径(groupName/path)
     * @param callback
     * @return
     */
    boolean downloadFile(String filePath, DownloadCallback callback);

    /**
     * 下载整个文件到DownloadCallback
     *
     * @param filePath 文件路径(groupName/path)
     * @param offset
     * @param size
     * @param callback
     * @return
     */
    boolean downloadFile(String filePath, long offset, long size, DownloadCallback callback);

    /**
     * 下载整个文件到DownloadCallback
     *
     * @param groupName
     * @param path
     * @param callback
     * @return
     */
    boolean downloadFile(String groupName, String path, DownloadCallback callback);

    /**
     * 下载文件片段到DownloadCallback
     *
     * @param groupName
     * @param path
     * @param offset
     * @param size
     * @param callback
     * @return
     */
    boolean downloadFile(String groupName, String path, long offset, long size, DownloadCallback callback);

    //todo 封装和完善所有api ==>>FastDfsBuilder构建返回该接口

}