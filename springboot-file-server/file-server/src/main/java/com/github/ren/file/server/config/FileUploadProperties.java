package com.github.ren.file.server.config;

import com.github.ren.file.client.starter.StorageProperties;
import com.github.ren.file.client.starter.StorageType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * @Description:
 * @date 2020/6/10 11:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    @Autowired
    private StorageProperties storageProperties;

    private StorageType storage;

    public FileUploadProperties(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.storage = storageProperties.getType();
    }

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


    /**
     * 获取存储类型对应的int值
     *
     * @return
     */
    public int getStorageTypeInt() {
        StorageType[] types = StorageType.values();
        for (int i = 0; i < types.length; i++) {
            StorageType type = types[i];
            if (type.equals(storage)) {
                return i + 1;
            }
        }
        return 0;
    }

    public String getStorageTypeName() {
        StorageType[] types = StorageType.values();
        for (StorageType type : types) {
            if (type.equals(storage)) {
                return storage.name();
            }
        }
        return null;
    }

    public long getMinPartSize() {
        StorageType[] types = StorageType.values();
        for (StorageType type : types) {
            if (type.equals(storage)) {
                //5MB
                if (storage == StorageType.Minio) {
                    return 1024 * 1024 * 5;
                }
            }
        }
        //100KB
        return 1024 * 100;
    }

}
