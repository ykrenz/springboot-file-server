package com.github.ren.file.sdk.part;

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
     * 初始化上传 获取上传唯一标识
     *
     * @param yourObjectName 上传yourObjectName
     * @return 上传唯一标识
     */
    String initiateMultipartUpload(String yourObjectName);

    /**
     * 上传分片
     *
     * @param part 分片
     */
    String uploadPart(UploadPart part);

    /**
     * 获取分片列表
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     * @return 分片信息
     */
    List<PartInfo> listParts(String uploadId, String yourObjectName);

    /**
     * 获取上传分片列表
     *
     * @param uploadId       分片标识
     * @param yourObjectName 上传yourObjectName
     * @return 分片数据
     */
    List<UploadPart> listUploadParts(String uploadId, String yourObjectName);

    /**
     * 获取上传分片
     *
     * @param uploadId       分片标识
     * @param yourObjectName 上传yourObjectName
     * @param partNumber     分片索引
     * @return
     */
    UploadPart getUploadPart(String uploadId, String yourObjectName, Integer partNumber);

    /**
     * 删除分片数据
     *
     * @param uploadId       上传唯一标识
     * @param yourObjectName 上传yourObjectName
     * @return 是否清除完成
     */
    boolean clear(String uploadId, String yourObjectName);
}
