package com.ykrenz.file.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileModel {

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
     * crc
     */
    private String crc;

    /**
     * md5
     */
    private String md5;
}
