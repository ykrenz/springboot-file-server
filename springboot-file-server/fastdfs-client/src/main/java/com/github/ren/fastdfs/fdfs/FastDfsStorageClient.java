package com.github.ren.fastdfs.fdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.DownloadCallback;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;

import java.io.File;
import java.io.InputStream;
import java.util.List;

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
    TrackerGroup trackerGroup();

    /**
     * 获取TrackerClient
     *
     * @return
     */
    TrackerClient trackerClient();

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
     * @return
     */
    boolean overwriteMetadata(String filePath, NameValuePair[] metaData);

    /**
     * 修改文件元信息（覆盖）
     *
     * @param groupName
     * @param path
     * @param metaData
     * @return
     */
    boolean overwriteMetadata(String groupName, String path, NameValuePair[] metaData);

    /**
     * 修改文件元信息（合并）
     *
     * @param filePath 文件路径(groupName/path)
     * @param metaData
     * @return
     */
    boolean mergeMetadata(String filePath, NameValuePair[] metaData);

    /**
     * 修改文件元信息（合并）
     *
     * @param groupName
     * @param path
     * @param metaData
     * @return
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
     * @param offset
     * @param size
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

    String uploadAppenderFile(String localFile);

    String uploadAppenderFile(String localFile, NameValuePair[] metaData);

    String uploadAppenderFile(InputStream inputStream, String fileExtName);

    String uploadAppenderFile(InputStream inputStream, String fileExtName, NameValuePair[] metaData);

    String uploadAppenderFile(String groupName, InputStream inputStream, String fileExtName, NameValuePair[] metaData);

    boolean appendFile(String filePath, String localFile);

    boolean appendFile(String groupName, String path, String localFile);

    boolean appendFile(String filePath, InputStream inputStream);

    boolean appendFile(String groupName, String path, InputStream inputStream);

    boolean appendFile(String groupName, String path, InputStream inputStream, int offset, int length);

    boolean modifyFile(String filePath, String localFile, long offset);

    boolean modifyFile(String filePath, InputStream inputStream, long offset);

    boolean modifyFile(String groupName, String path, String localFile, long offset);

    boolean modifyFile(String groupName, String path, InputStream inputStream, long offset);

    boolean truncateFile(String filePath);

    boolean truncateFile(String filePath, long truncatedFileSize);

    boolean truncateFile(String groupName, String path);

    boolean truncateFile(String groupName, String path, long truncatedFileSize);

    String initiateMultipartUpload(long fileSize, long partSize, String fileExtName);

    FastPart uploadPart(String filePath, int partNumber, String localFile);

    FastPart uploadPart(String filePath, int partNumber, InputStream inputStream);

    String completeMultipartUpload(String filePath);

    List<FastPart> listParts(String filePath);

    boolean abortMultipartUpload(String filePath);

    //todo 封装和完善所有api ==>>FastDfsBuilder构建返回该接口

}