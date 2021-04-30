package com.github.ren.file.sdk.part;

/**
 * @Description 分片上传客户端
 * @Author ren
 * @Since 1.0
 */
public interface UploadPartClient extends OssPartClient {
    /**
     * 合并分片上传
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 文件位置
     * @return 文件地址
     */
    String merge(String uploadId, String yourObjectName);

    /**
     * 取消分片数据
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 文件位置
     */
    void cancel(String uploadId, String yourObjectName);
}
