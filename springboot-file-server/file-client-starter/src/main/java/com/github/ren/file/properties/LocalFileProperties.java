package com.github.ren.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地化存储配置类
 */
@ConfigurationProperties(prefix = "file.local")
public class LocalFileProperties {
    private String webServerUrl;
    private String fileStoragePath = "/data/storage/file/";

    public String getWebServerUrl() {
        return webServerUrl;
    }

    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }

    public String getFileStoragePath() {
        return fileStoragePath;
    }

    public void setFileStoragePath(String fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }
}