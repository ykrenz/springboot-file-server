package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class PartResponse {

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
