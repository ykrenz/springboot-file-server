package com.github.ren.file.sdk.part;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @Description 分片信息
 * @Author ren
 * @Since 1.0
 */
@Data
@AllArgsConstructor
public class UploadPart implements Serializable {
    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * 上传唯一标识
     */
    private String key;

    /**
     * 分片索引
     */
    private int partNumber;

    /**
     * 分片大小
     */
    private long partSize;

    /**
     * 分片文件流信息
     */
    private InputStream inputStream;

    /**
     * 分片md5值 如果添加了则会校验md5是否正确
     */
    private String md5Digest;

    public UploadPart(String uploadId, String key, int partNumber, long partSize, InputStream inputStream) {
        this.uploadId = uploadId;
        this.key = key;
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.inputStream = inputStream;
    }
}
