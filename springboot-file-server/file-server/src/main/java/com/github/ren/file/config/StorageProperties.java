package com.github.ren.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * @Description: upload配置
 * @date 2020/6/10 11:35
 */
@Data
@Component
@ConfigurationProperties(StorageProperties.Prefix)
public class StorageProperties {

    public static final String Prefix = "file";
    /**
     * 存储方式
     */
    private StorageType storage = StorageType.Local;

    private String bucketName;
    /**
     * 普通上传支持最大文件Size
     */
    private DataSize maxUploadSize = DataSize.ofMegabytes(1L);
    /**
     * 上传分片支持最小Size
     */
    private DataSize multipartMinSize = DataSize.ofBytes(1L);
    /**
     * 上传分片支持最大Size
     */
    private DataSize multipartMaxSize = DataSize.ofBytes(1L);

}
