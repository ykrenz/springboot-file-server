package com.github.ren.file.sdk.part;

import java.util.List;

/**
 * @Description 分片上传处理客户端
 * @Author ren
 * @Since 1.0
 */
public interface PartStore extends OssPartClient {
    /**
     * 获取上传分片列表
     *
     * @param uploadId 分片标识
     * @return 分片数据
     */
    List<UploadPart> listUploadParts(String uploadId);

    /**
     * 删除分片数据
     *
     * @param uploadId       上传唯一标识
     */
    void del(String uploadId);
}
