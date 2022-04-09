package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class InitRequest {

    /**
     * 文件名称
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
     * 分片起始值
     */
    private int partIndex;
}
