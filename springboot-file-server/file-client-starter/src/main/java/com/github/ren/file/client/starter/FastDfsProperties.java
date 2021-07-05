package com.github.ren.file.client.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description fastDfs配置
 * @Author ren
 * @Since 1.0
 */
@ConfigurationProperties(Constants.FastDfsPrefix)
@Data
public class FastDfsProperties {
//    https://github.com/happyfish100/fastdfs-client-java
//    fastdfs.connect_timeout_in_seconds = 5
//    fastdfs.network_timeout_in_seconds = 30
//    fastdfs.charset = UTF-8
//    fastdfs.http_anti_steal_token = false
//    fastdfs.http_secret_key = FastDFS1234567890
//    fastdfs.http_tracker_http_port = 80
//
//    fastdfs.tracker_servers = 10.0.11.201:22122,10.0.11.202:22122,10.0.11.203:22122
//
//    fastdfs.connection_pool.enabled = true
//    fastdfs.connection_pool.max_count_per_entry = 500
//    fastdfs.connection_pool.max_idle_time = 3600
//    fastdfs.connection_pool.max_wait_time_in_ms = 1000

    /**
     * 文件上传的group
     */
    private String group;
    /**
     * 连接超时时间 单位/s
     */
    private int connectTimeoutSeconds;

    /**
     * socket连接超时时间 单位/s
     */
    private long networkTimeoutSeconds;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 是否使用Token
     */
    private boolean httpAntiStealToken;

    /**
     * Token加密密钥
     */
    private String httpSecretKey;

    /**
     * 跟踪器IP地址，多个使用分号隔开
     */
    private int httpTrackerHttpPort;

    /**
     * trackerServers 多个server用逗号隔开
     */
    private String trackerServers;


    /**
     * 连接池配置
     */
    private Pool pool;

    @Data
    static class Pool {
        /**
         * 是否开启连接池
         */
        private boolean enabled = true;

        /**
         * 最大连接数 超过会等待maxWaitTime新建一个连接
         */
        private int maxCount;

        /**
         * 最大空闲时间 单位/ms
         */
        private int maxIdleTime;

        /**
         * 最大等待时间 单位/ms
         */
        private long maxWaitTime;
    }

}
