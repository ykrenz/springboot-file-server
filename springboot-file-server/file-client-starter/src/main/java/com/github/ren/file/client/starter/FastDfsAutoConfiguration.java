package com.github.ren.file.client.starter;

import com.github.ren.file.client.fdfs.FastDfsClientBuilder;
import com.github.ren.file.client.fdfs.FastDfsClientConfiguration;
import com.github.ren.file.client.fdfs.FastDfsStorageClient;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.ren.file.client.starter.Constants.StoragePrefix;

/**
 * fastdfs客户端自动配置
 */

@ConditionalOnClass(FastDfsStorageClient.class)
@EnableConfigurationProperties({FastDfsProperties.class, StorageProperties.class})
@Configuration
public class FastDfsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = StoragePrefix, name = "type", havingValue = "FastDfs")
    public FastDfsStorageClient fastDfsStorageClient(FastDfsProperties fastDfsProperties) {
        FastDfsClientConfiguration clientConfiguration = new FastDfsClientConfiguration();
        BeanUtils.copyProperties(fastDfsProperties, clientConfiguration);
        BeanUtils.copyProperties(fastDfsProperties.getPool(), clientConfiguration.getPool());
        String trackerServers = fastDfsProperties.getTrackerServers();
        String groupName = fastDfsProperties.getGroupName();
        return new FastDfsClientBuilder().build(trackerServers, groupName, clientConfiguration);
    }
}
