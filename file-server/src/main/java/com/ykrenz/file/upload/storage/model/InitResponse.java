package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class InitResponse {

    /**
     * 分片唯一标识
     */
    private String uploadId;

    /**
     * 创建时间
     */
    private long createTime;
}
