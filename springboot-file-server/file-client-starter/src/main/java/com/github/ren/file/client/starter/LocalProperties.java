package com.github.ren.file.client.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 本地存储配置
 * @Author ren
 * @Since 1.0
 */
@ConfigurationProperties(Constants.LocalPrefix)
@Data
public class LocalProperties {
}
