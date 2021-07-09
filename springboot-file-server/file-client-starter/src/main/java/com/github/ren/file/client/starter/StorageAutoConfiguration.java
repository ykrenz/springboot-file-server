package com.github.ren.file.client.starter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.ren.file.client.fdfs.FastDfsClientBuilder;
import com.github.ren.file.client.fdfs.FastDfsClientConfiguration;
import com.github.ren.file.client.fdfs.FastDfsStorageClient;
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
        S3Properties.class
})
@Configuration
public class StorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = StorageTypePrefix, havingValue = "s3")
    public AmazonS3 amazonS3(S3Properties s3Properties) {
        String accessKey = s3Properties.getAccessKey();
        String secretKey = s3Properties.getSecretKey();
        String endpoint = s3Properties.getEndpoint();
        String region = s3Properties.getRegion();
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.
                        EndpointConfiguration(endpoint, region);

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = StorageTypePrefix, havingValue = "FastDfs")
    public FastDfsStorageClient fastDfsStorageClient(FastDfsProperties fastDfsProperties) {
        FastDfsClientConfiguration clientConfiguration = new FastDfsClientConfiguration();
        BeanUtils.copyProperties(fastDfsProperties, clientConfiguration);
        BeanUtils.copyProperties(fastDfsProperties.getPool(), clientConfiguration.getPool());
        String trackerServers = fastDfsProperties.getTrackerServers();
        String groupName = fastDfsProperties.getGroupName();
        return new FastDfsClientBuilder().build(trackerServers, groupName, clientConfiguration);
    }
}
