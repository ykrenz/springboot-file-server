package com.github.ren.file.sdk.part;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分片处理客户端
 * @Author ren
 * @Since 1.0
 */
public interface OssPartClient extends Serializable {
    /**
     * 初始化上传 获取上传唯一标识
     *
     * @return 上传唯一标识
     */
    String initUpload();

    /**
     * 上传分片
     *
     * @param part 分片
     */
    void uploadPart(UploadPart part);

    /**
     * 获取分片列表
     *
     * @param uploadId 上传唯一标识
     * @return 分片信息
     */
    List<PartInfo> listParts(String uploadId);


}
