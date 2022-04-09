package com.ykrenz.file.upload.manager;

import lombok.Data;

@Data
public class PartModel {

    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * objectName
     */
    private String objectName;

    /**
     * 分片索引
     */
    private int partNumber;

    /**
     * 分片大小
     */
    private long size;

    /**
     * 分片eTag
     */
    private String eTag;
}