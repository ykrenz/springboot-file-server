package com.github.ren.file.client.part;

import lombok.Builder;
import lombok.Data;

/**
 * @Description 初始化分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Builder
@Data
public class InitMultipartUploadArgs {

    /**
     * 文件存储位置 oss使用
     */
    private String objectName;

    /**
     * 文件大小 fastdfs使用
     */
    private long fileSize;

}
