package com.github.ren.file.sdk.part;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分片处理客户端
 * @Author ren
 * @Since 1.0
 */
public interface OssPartClient extends Serializable {

    // <objectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

    /**
     * 初始化上传 获取上传唯一标识
     *
     * @param objectName 上传objectName
     * @return InitMultipartResult
     */
    InitMultipartResult initiateMultipartUpload(String objectName);

    /**
     * 上传分片
     *
     * @param part 分片
     * @return 分片信息
     */
    PartInfo uploadPart(UploadPart part);

    /**
     * 获取所有的分片列表
     *
     * @param uploadId       上传唯一标识
     * @param objectName 上传yourObjectName
     * @return 分片信息
     */
    List<PartInfo> listParts(String uploadId, String objectName);

    /**
     * 完成上传
     *
     * @param uploadId       上传唯一标识
     * @param objectName 上传yourObjectName
     * @param parts          合并的分片信息
     * @return
     */
    CompleteMultipart completeMultipartUpload(String uploadId, String objectName, List<PartInfo> parts);

    /**
     * 终止上传
     *
     * @param uploadId       上传唯一标识
     * @param objectName 上传yourObjectName
     */
    void abortMultipartUpload(String uploadId, String objectName);

}
