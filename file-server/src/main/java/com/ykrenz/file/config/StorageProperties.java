package com.ykrenz.file.config;

import com.ykrenz.file.upload.storage.StorageType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * @Description: upload配置
 * @date 2020/6/10 11:35
 */
@Data
@ConfigurationProperties("file")
public class StorageProperties {
    /**
     * 存储方式
     */
    private StorageType storage = StorageType.fastdfs;
    /**
     * 上传分片支持最小Size
     */
    private DataSize multipartMinSize = DataSize.ofBytes(1L);
    /**
     * 上传分片支持最大Size
     */
    private DataSize multipartMaxSize = DataSize.ofBytes(1L);
    /**
     * 上传过期天数 默认7天
     */
    private int expireDay = 7;
}
