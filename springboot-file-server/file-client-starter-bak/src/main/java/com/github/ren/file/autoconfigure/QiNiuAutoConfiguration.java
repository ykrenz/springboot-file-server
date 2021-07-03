package com.github.ren.file.server;

import com.github.ren.file.clients.QiNiuClient;
import com.github.ren.file.clients.QiNiuFileClient;
import com.github.ren.file.properties.QiNiuProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛自动配置
 */
@EnableConfigurationProperties(QiNiuProperties.class)
@Configuration
public class QiNiuAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public QiNiuClient qiNiuClient(QiNiuProperties qiNiuProperties) {
        return new QiNiuFileClient(qiNiuProperties);
    }
}
