package com.github.ren.file.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * file客户端自动配置
 */
@EnableConfigurationProperties(FileServerProperties.class)
@Configuration
public class FileClientAutoConfiguration {

}
