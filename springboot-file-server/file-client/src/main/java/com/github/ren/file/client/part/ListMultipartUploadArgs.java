package com.github.ren.file.client.part;

import lombok.Builder;
import lombok.Data;

/**
 * @Description 分片查询上传参数
 * @Author ren
 * @Since 1.0
 */
@Builder
@Data
public class ListMultipartUploadArgs {

    /**
     * 分片上传标识
     */
    private String uploadId;

    /**
     * 文件存储位置
     */
    private String objectName;
}
