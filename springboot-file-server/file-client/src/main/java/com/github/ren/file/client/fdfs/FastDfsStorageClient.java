package com.github.ren.file.client.fdfs;

import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

import java.io.InputStream;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public interface FastDfsStorageClient {
    /**
     * 获取客户端group分组
     *
     * @return 没有设置则返回null 上传时自动进行分组
     */
    String getClientGroup();

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
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @return 上传后的文件路径
     */
    String uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName);

    //todo 封装和完善所有api ==>>FastDfsBuilder构建返回该接口

}