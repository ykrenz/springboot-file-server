package com.github.ren.file.sdk.part;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分片处理客户端
 * @Author ren
 * @Since 1.0
 */
public interface OssPartClient extends Serializable {

    // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

    /**
     * 初始化上传 获取上传唯一标识
     *
     * @param yourObjectName 上传yourObjectName
     * @return InitMultipartResult
     */
    InitMultipartResult initiateMultipartUpload(String yourObjectName);

    /**
     * 上传分片
     *
     * @param part 分片
     * @return 分片信息
     */
    PartInfo uploadPart(UploadPart part);

    /**
     * 获取分片列表
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     * @return 分片信息
     */
    List<PartInfo> listParts(String uploadId, String yourObjectName);

    /**
     * 完成上传
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     * @return
     */
    CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName);

    /**
     * 终止上传
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     */
    void abortMultipartUpload(String uploadId, String yourObjectName);

}
