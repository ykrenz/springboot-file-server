package com.github.ren.file.sdk.model;

import lombok.Data;

/**
 * @Description 通用请求实体
 * @Author ren
 * @Since 1.0
 */
@Data
public abstract class UploadGenericRequest {

    private String bucketName;

    private String objectName;

    public UploadGenericRequest(String objectName) {
        this.objectName = objectName;
    }

    public UploadGenericRequest(String bucketName, String objectName) {
        this.bucketName = bucketName;
        this.objectName = objectName;
    }
}
