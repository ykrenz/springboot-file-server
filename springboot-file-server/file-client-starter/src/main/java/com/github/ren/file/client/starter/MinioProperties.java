package com.github.ren.file.client.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description minio配置
 * @Author ren
 * @Since 1.0
 */
@ConfigurationProperties(Constants.MinioPrefix)
@Data
public class MinioProperties {
    /**
     * 文件访问前缀地址
     * 如果Object是公共读/公共读写权限，那么文件URL的格式为：BucketName.Endpoint/ObjectName
     * 如果Object是私有权限，则必须进行签名操作。文件URL的格式为：BucketName.Endpoint/Object?签名参数
     * 如果Object所在的Bucket绑定了自定义域名，那么文件URL的格式为：YourDomainName/ObjectName
     * 具体详情可访问https://help.aliyun.com/knowledge_detail/39607.html
     * 也可以配置nginx的地址进行反向代理等。在此不做解释
     */
    private String webServerUrl;
    /**
     * your oss endpoint
     */
    private String endpoint;
    /**
     * your oss accessKey
     */
    private String accessKey;
    /**
     * your oss secretKey
     */
    private String secretKey;
    /**
     * your oss bucketName
     */
    private String bucketName;


}