package com.github.ren.file.client.fdfs;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public interface FastDfsStorageClient {

    /**
     * 获取当前client  TrackerServer
     *
     * @return StorageServer信息
     */
    TrackerServer getTrackerServer();

    /**
     * 获取当前client  StorageServer
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
     * @param inputStream
     * @param fileExtName
     * @return
     */
    String uploadFile(String groupName, InputStream inputStream, String fileExtName);

    /**
     * 上传一般文件
     *
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @param metaDataSet
     * @return
     */
    String uploadFile(InputStream inputStream, String fileExtName, Set<MetaData> metaDataSet);


    /**
     * 上传从文件
     *
     * @param masterFilename 主文件路径
     * @param prefixName     从文件前缀
     * @param inputStream
     * @param fileExtName
     * @return
     */
    String uploadSlaveFile(String masterFilename, String prefixName, InputStream inputStream, String fileExtName);

    /**
     * 删除文件
     *
     * @param filePath 文件路径(groupName/path)
     * @return
     */
    boolean deleteFile(String filePath);

//    /**
//     * 获取文件元信息
//     *
//     * @param groupName
//     * @param path
//     * @return
//     */
//    Set<MetaData> getMetadata(String groupName, String path);
//
//    /**
//     * 修改文件元信息（覆盖）
//     *
//     * @param groupName
//     * @param path
//     * @param metaDataSet
//     */
//    void overwriteMetadata(String groupName, String path, Set<MetaData> metaDataSet);
//
//    /**
//     * 修改文件元信息（合并）
//     *
//     * @param groupName
//     * @param path
//     * @param metaDataSet
//     */
//    void mergeMetadata(String groupName, String path, Set<MetaData> metaDataSet);

//    /**
//     * 查看文件的信息
//     *
//     * @param groupName
//     * @param path
//     * @return
//     */
//    FileInfo queryFileInfo(String groupName, String path);

    /**
     * 删除文件
     *
     * @param groupName
     * @param path
     * @return
     */
    boolean deleteFile(String groupName, String path);

//    /**
//     * 下载整个文件
//     *
//     * @param groupName
//     * @param path
//     * @param callback
//     * @return
//     */
//    <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback);
//
//    /**
//     * 下载文件片段
//     *
//     * @param groupName
//     * @param path
//     * @param fileOffset
//     * @param fileSize
//     * @param callback
//     * @return
//     */
//    <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback);

    //todo 封装和完善所有api ==>>FastDfsBuilder构建返回该接口

}