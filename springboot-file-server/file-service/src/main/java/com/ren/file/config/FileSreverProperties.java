package com.ren.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @date 2020/6/10 11:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.server")
public class FileSreverProperties {
    private String chunkTempPath;
}
