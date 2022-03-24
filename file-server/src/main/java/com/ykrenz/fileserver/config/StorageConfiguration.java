package com.ykrenz.fileserver.config;

import com.ykrenz.fileserver.service.client.FastDfsClearApplicationListener;
import com.ykrenz.fileserver.service.client.FastDfsServerClient;
import com.ykrenz.fileserver.service.client.FileServerClient;
import com.ykrenz.fastdfs.FastDfs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfiguration {

    @Bean
    @ConditionalOnMissingBean()
    @ConditionalOnProperty(value = "file.storage", havingValue = "fastdfs")
    public FileServerClient fstDfsClient(FastDfs fastDfs) {
        FastDfsServerClient client = new FastDfsServerClient(fastDfs);
        new FastDfsClearApplicationListener(client);
        return client;
    }

    //TODO 定期删除过期分片任务
}
