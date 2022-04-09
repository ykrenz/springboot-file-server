package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class CompleteRequest {

    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * crc校验码
     */
    private String crc;
}
