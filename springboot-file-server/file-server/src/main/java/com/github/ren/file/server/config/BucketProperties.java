package com.github.ren.file.server.config;

import com.github.ren.file.client.starter.Constants;
import com.github.ren.file.client.starter.S3Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(Constants.S3Prefix)
@Component
public class BucketProperties extends S3Properties {
    private String bucketName;
}
