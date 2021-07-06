package com.github.ren.file.client.starter;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.ren.file.client.ali.AliOssClient;
import com.github.ren.file.client.fdfs.FastDfsClient;
import com.github.ren.file.client.fdfs.FastDfsClientBuilder;
import com.github.ren.file.client.fdfs.FastDfsClientConfiguration;
import com.github.ren.file.client.minio.MinIoClient;
import com.github.ren.file.client.minio.MinIoClientBuilder;
import io.minio.MinioClient;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.ren.file.client.starter.Constants.StorageTypePrefix;

/**
 * file客户端自动配置
 */
@EnableConfigurationProperties({
        StorageProperties.class,
        FastDfsProperties.class,
        AliYunOssProperties.class,
        MinioProperties.class
})
@Configuration
public class FileClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = StorageTypePrefix, havingValue = "FastDfs")
    public FastDfsClient fastDfsClient(FastDfsProperties fastDfsProperties) {
        FastDfsClientConfiguration clientConfiguration = new FastDfsClientConfiguration();
        BeanUtils.copyProperties(fastDfsProperties, clientConfiguration);
        BeanUtils.copyProperties(fastDfsProperties.getPool(), clientConfiguration.getPool());
        return new FastDfsClientBuilder().build(clientConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = StorageTypePrefix, havingValue = "AliOss")
    public AliOssClient aliOssClient(AliYunOssProperties aliYunOssProperties) {
        String accessKey = aliYunOssProperties.getAccessKey();
        String secretKey = aliYunOssProperties.getSecretKey();
        String bucketName = aliYunOssProperties.getBucketName();
        String endpoint = aliYunOssProperties.getEndpoint();
        OSS oss = new OSSClientBuilder()
                .build(endpoint,
                        accessKey,
                        secretKey);
        return new AliOssClient(oss, bucketName);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = StorageTypePrefix, havingValue = "Minio")
    public MinIoClient minIoClient(MinioProperties minioProperties) {
        String accessKey = minioProperties.getAccessKey();
        String secretKey = minioProperties.getSecretKey();
        String bucketName = minioProperties.getBucketName();
        String endpoint = minioProperties.getEndpoint();
        MinioClient client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return MinIoClientBuilder.build(client, bucketName);
    }
}
