package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class CompleteRequest {

    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * 校验码
     */
    private String hash;
}
