package com.github.ren.file.client.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description s3协议通用配置
 * @Author ren
 * @Since 1.0
 */
@ConfigurationProperties(Constants.S3Prefix)
@Setter
@Getter
public class S3Properties {

    /**
     * your accessKey
     */
    private String accessKey;
    /**
     * your secretKey
     */
    private String secretKey;
    /**
     * your endpoint
     */
    private String endpoint;
    /**
     * your region
     */
    private String region;

}