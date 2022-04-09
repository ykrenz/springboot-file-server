package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class UploadResponse {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * 文件路径
     */
    private String objectName;

    /**
     * crc校验码
     */
    private String crc;
}
