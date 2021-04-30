package com.github.ren.file.sdk.part;

import java.util.List;

/**
 * @Description 分片处理客户端
 * @Author ren
 * @Since 1.0
 */
public interface PartClient {
    /**
     * 初始化上传
     *
     * @return
     */
    String initUpload();

    /**
     * 上传分片
     *
     * @param part
     */
    void uploadPart(UploadPart part);

    /**
     * 上传多个分片
     *
     * @param parts
     */
    void uploadParts(List<UploadPart> parts);

    /**
     * 获取分片列表
     *
     * @param uploadId 分片标识 本地或者redis 可以用md5值 oss取initUpload()的返回值
     * @return
     */
    List<PartInfo> listParts(String uploadId);
}
