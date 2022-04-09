package com.ykrenz.file.dao.mapper.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileEntity extends BaseEntity {

    /**
     * 文件存储id
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 客户端id
     */
    private String clientId;

}
