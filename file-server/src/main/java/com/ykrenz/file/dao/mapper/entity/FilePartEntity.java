package com.ykrenz.file.dao.mapper.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件分片
 *
 * @author ykren
 * @date 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FilePartEntity extends BaseEntity {

    /**
     * 分片上传唯一标识
     */
    private String uploadId;

    /**
     * 分片索引
     */
    private Integer partNumber;

    /**
     * 分片文件大小
     */
    private Long fileSize;

    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * objectName
     */
    private String objectName;

}
