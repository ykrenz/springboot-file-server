package com.ykrenz.file.dao.mapper.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件信息
 *
 * @author ykren
 * @date 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileStorageEntity extends BaseEntity {

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

}
