package com.github.ren.file.config;

import com.github.ren.file.model.ChunkType;
import com.github.ren.file.model.StorageType;
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
public class FileServerProperties {
    /**
     * 临时文件目录
     */
    private String chunkTempPath;
    /**
     * 存储方式
     */
    private StorageType storage = StorageType.LOCAL;

    /**
     * 块文件存储方式
     */
    private ChunkType chunk = ChunkType.LOCAL;
}
