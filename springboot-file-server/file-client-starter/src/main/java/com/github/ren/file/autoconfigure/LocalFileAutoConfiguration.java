package com.github.ren.file.autoconfigure;

import com.github.ren.file.clients.LocalClient;
import com.github.ren.file.clients.LocalFileClient;
import com.github.ren.file.properties.LocalFileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地存储自动配置
 */
@EnableConfigurationProperties(LocalFileProperties.class)
@Configuration
public class LocalFileAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LocalClient localClient(LocalFileProperties localFileProperties) {
        return new LocalFileClient(localFileProperties);
    }
}
