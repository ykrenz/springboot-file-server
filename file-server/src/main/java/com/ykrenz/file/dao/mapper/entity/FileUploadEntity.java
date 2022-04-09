package com.ykrenz.file.dao.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("file_upload")
public class FileUploadEntity extends BaseEntity {

    /**
     * 分片上传唯一标识
     */
    private String uploadId;

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
