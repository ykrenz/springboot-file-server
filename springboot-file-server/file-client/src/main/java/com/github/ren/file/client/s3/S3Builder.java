package com.github.ren.file.client.s3;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
public interface S3Builder {
    S3 build(String endpoint, String accessKeyId, String secretAccessKey);
}
