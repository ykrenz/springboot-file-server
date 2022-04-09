package com.ykrenz.file.config;

import com.ykrenz.file.upload.manager.FileUploadClear;
import com.ykrenz.file.upload.storage.FileServerClient;
import com.ykrenz.file.upload.storage.StorageType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfiguration {

    private final StorageProperties storageProperties;

    public StorageConfiguration(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Bean
    public Map<StorageType, FileServerClient> storageClientMap(List<FileServerClient> clients) {
        Map<StorageType, FileServerClient> storageClientMap = new HashMap<>(clients.size());
        clients.forEach(o -> storageClientMap.put(o.type(), o));
        return storageClientMap;
    }

    @Configuration
    static class ClearUploadConfiguration {


        private final FileUploadClear fileUploadClear;
        private final long uploadClearSeconds = 3600L;
        private final int uploadExpireDay;

        public ClearUploadConfiguration(FileUploadClear fileUploadClear, StorageProperties storageProperties) {
            this.fileUploadClear = fileUploadClear;
            this.uploadExpireDay = storageProperties.getExpireDay();
        }

        @Scheduled(fixedDelay = uploadClearSeconds)
        public void clearUpload() {
            fileUploadClear.clear(uploadExpireDay);
        }
    }

}
