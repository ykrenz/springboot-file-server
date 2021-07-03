package com.github.ren.file.client.part;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分片存储客户端
 * @Author ren
 * @Since 1.0
 */
public interface PartStore extends Serializable {

    // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg

    /**
     * 上传分片
     *
     * @param part 分片
     */
    String uploadPart(UploadPartArgs part);

    /**
     * 获取分片列表
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     * @return 分片信息
     */
    List<UploadMultipartResponse> listParts(String uploadId, String yourObjectName);

    /**
     * 获取上传分片
     *
     * @param uploadId       分片标识
     * @param yourObjectName 上传yourObjectName
     * @param partNumber     分片索引
     * @return
     */
    UploadPartArgs getUploadPart(String uploadId, String yourObjectName, Integer partNumber);

    /**
     * 删除分片数据
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     * @return 是否清除完成
     */
    boolean clear(String uploadId, String yourObjectName);
}
