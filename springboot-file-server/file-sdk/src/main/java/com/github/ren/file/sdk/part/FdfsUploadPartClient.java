package com.github.ren.file.sdk.part;

/**
 * @Description fastdfs分片上传客户端
 * @Author ren
 * @Since 1.0
 */
public interface FdfsUploadPartClient extends PartClient {

    /**
     * 完成分片上传
     *
     * @param uploadId
     * @param ext      文件后缀
     * @return
     */
    String complete(String uploadId, String ext);

    /**
     * 取消分片数据
     *
     * @param uploadId
     * @param yourObjectName 文件位置
     * @param groupName      文件组
     * @param path           文件地址
     */
    void cancel(String uploadId, String yourObjectName, String groupName, String path);

}
