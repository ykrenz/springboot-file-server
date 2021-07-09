package com.github.ren.file.client;

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
    InitMultipartResponse initMultipartUpload(InitMultipartUploadArgs args);

    /**
     * 上传分片
     *
     * @param args
     * @return
     */
    UploadPartResponse uploadMultipart(UploadMultiPartArgs args);

    /**
     * 获取所有的分片列表
     *
     * @param args
     * @return
     */
    List<UploadPartResponse> listParts(ListPartsArgs args);

    /**
     * 完成上传
     *
     * @param args
     * @return
     */
    CompleteMultipartResponse completeMultipartUpload(CompleteMultiPartArgs args);

    /**
     * 终止上传
     *
     * @param args
     */
    void abortMultipartUpload(AbortMultiPartArgs args);

}
