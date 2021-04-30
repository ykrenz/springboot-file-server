package com.github.ren.file.clients.ali;

import com.aliyun.oss.OSS;
import com.github.ren.file.clients.UploadClient;

/**
 * @Description 阿里云oss客户端
 * @Author ren
 * @Since 1.0
 */
public class AliOssClient implements UploadClient {
    private OSS oss;

    public AliOssClient(OSS oss) {
        this.oss = oss;
    }


}
