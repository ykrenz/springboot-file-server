package com.github.ren.file.autoconfigure;

import com.github.ren.file.clients.FastDfsClient;
import com.github.ren.file.clients.FastDfsFileClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fastdfs自动配置
 */
@Configuration
public class FastDfsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FastDfsClient fastDfsClient() {
        return new FastDfsFileClient();
    }
}
