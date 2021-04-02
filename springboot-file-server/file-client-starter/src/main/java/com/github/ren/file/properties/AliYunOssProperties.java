package com.github.ren.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云oss存储配置类
 */
@ConfigurationProperties(prefix = "file.ali")
public class AliYunOssProperties {
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
     * your oss accessKeyId
     */
    private String accessKeyId;
    /**
     * your oss accessKeySecret
     */
    private String accessKeySecret;
    /**
     * your oss bucketName
     */
    private String bucketName;

    public String getWebServerUrl() {
        return webServerUrl;
    }

    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

}