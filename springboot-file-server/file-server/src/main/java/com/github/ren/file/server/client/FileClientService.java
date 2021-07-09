package com.github.ren.file.server.client;

import com.github.ren.file.client.starter.StorageType;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分片处理客户端
 * @Author ren
 * @Since 1.0
 */
public interface FileClientService extends Serializable {

    // <objectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

    /**
     * 初始化上传 获取上传唯一标识
     *
     * @param args
     * @return InitMultipartResult
     */
    InitMultipartResponse initMultipartUpload(StorageType storage, InitMultipartUploadArgs args);

    /**
     * 上传分片
     *
     * @param part 分片
     * @return 分片信息
     */
    PartResult uploadMultipart(StorageType storage, UploadMultiPartArgs part);

    /**
     * 获取所有的分片列表
     *
     * @param args
     * @return 分片信息
     */
    List<PartResult> listParts(StorageType storage, ListPartsArgs args);

    /**
     * 完成上传
     *
     * @param uploadId   上传唯一标识
     * @param objectName 上传yourObjectName
     * @param parts      合并的分片信息
     * @return
     */
    CompleteMultipartResponse completeMultipartUpload(StorageType storage, CompleteMultiPartArgs args);

    /**
     * 终止上传
     *
     * @param uploadId   上传唯一标识
     * @param objectName 上传yourObjectName
     */
    void abortMultipartUpload(StorageType storage, AbortMultiPartArgs args);

}
