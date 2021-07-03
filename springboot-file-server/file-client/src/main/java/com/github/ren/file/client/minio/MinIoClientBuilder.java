package com.github.ren.file.client.minio;

import io.minio.MinioClient;

/**
 * @Description minio文件客户端Builder
 * @Author ren
 * @Since 1.0
 */
public class MinIoClientBuilder {
    public static MinIoClient build(MinioClient minioClient) {
        return new MinIoClient(minioClient);
    }

    public static MinIoClient build(MinioClient minioClient, String bucketName) {
        return new MinIoClient(minioClient, bucketName);
    }
}
