package com.github.ren.file.autoconfigure;

import com.github.ren.file.clients.AliOssClient;
import com.github.ren.file.clients.AliOssFileClient;
import com.github.ren.file.properties.AliYunOssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里OSS自动配置
 */
@EnableConfigurationProperties(AliYunOssProperties.class)
@Configuration
public class AliOssAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssClient aliOssClient(AliYunOssProperties aliYunOssProperties) {
        return new AliOssFileClient(aliYunOssProperties);
    }
}
