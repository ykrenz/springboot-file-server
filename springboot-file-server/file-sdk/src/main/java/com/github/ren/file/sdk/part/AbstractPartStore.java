package com.github.ren.file.sdk.part;

import java.util.UUID;

/**
 * @Description 抽象分片存储客户端
 * @Author ren
 * @Since 1.0
 */
public abstract class AbstractPartStore implements PartStore {

    @Override
    public String initiateMultipartUpload(String yourObjectName) {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
