package com.github.ren.file.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.ren.file.model.StorageType;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.ali.AliClient;
import com.github.ren.file.sdk.fdfs.FastDFSClient;
import com.github.ren.file.sdk.local.LocalClient;
import com.github.ren.file.sdk.minio.MinIoClient;
import com.github.ren.file.sdk.part.LocalPartStore;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * file客户端自动配置
 */
@EnableConfigurationProperties(FileServerProperties.class)
@Configuration
public class FileClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileClient fileClient(FileServerProperties fileServerProperties) {
        StorageType storage = fileServerProperties.getStorage();
        switch (storage) {
            case ALI:
                AliClient aliClient = AliClient.getInstance();
                OSS oss = new OSSClientBuilder()
                        .build("",
                                "",
                                "");
                aliClient.setOss(oss);
                aliClient.setBucketName("");
                return aliClient;
            case FAST_DFS:
                return FastDFSClient.getInstance();
            case MINIO:
                MinioClient client = MinioClient.builder()
                        .endpoint("http://192.168.231.140:9000")
                        .credentials("admin", "12345678")
                        .build();
                MinIoClient minIoClient = MinIoClient.build(client);
                minIoClient.setBucketName("test");
                return minIoClient;
            default:
                return new LocalClient("F:\\oss\\upload", new LocalPartStore("F:\\oss\\part"));
        }
    }
}
