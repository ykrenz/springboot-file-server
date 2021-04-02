package com.github.ren.file.config;

import com.github.ren.file.clients.*;
import com.github.ren.file.model.StorageType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * file客户端自动配置
 */
@EnableConfigurationProperties(FileServerProperties.class)
@Configuration
public class FileClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileClient fileClient(AliOssClient aliOssFileServerClient,
                                       QiNiuClient qiNiuFileClient,
                                       FastDfsClient fastDfsFileClient,
                                       LocalClient localFileClient,
                                       FileServerProperties fileServerProperties) {
        StorageType storage = fileServerProperties.getStorage();
        switch (storage) {
            case ALI:
                return aliOssFileServerClient;
            case QINIU:
                return qiNiuFileClient;
            case FAST_DFS:
                return fastDfsFileClient;
            default:
                return localFileClient;
        }
    }
}
