package com.ykrenz.file.upload.manager;

import lombok.Data;

@Data
public class InitUploadModel {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 分片大小
     */
    private long partSize;

    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * objectName
     */
    private String objectName;

}
