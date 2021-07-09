package com.github.ren.file.client.fdfs;

import lombok.Data;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Data
public class FastDfsClientConfiguration {
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
     * 连接超时时间 单位/s
     */
    private int connectTimeoutSeconds = 5;

    /**
     * socket连接超时时间 单位/s
     */
    private long networkTimeoutSeconds = 30;

    /**
     * 字符集
     */
    private String charset = "UTF-8";

    /**
     * 是否使用Token
     */
    private boolean httpAntiStealToken = false;

    /**
     * Token加密密钥
     */
    private String httpSecretKey = "FastDFS1234567890";

    /**
     * 跟踪器IP地址，多个使用分号隔开
     */
    private int httpTrackerHttpPort = 80;

    /**
     * 连接池配置
     */
    private Pool pool = new Pool();

    @Data
    public static class Pool {
        /**
         * 是否开启连接池
         */
        private boolean enabled = true;

        /**
         * 最大连接数 超过会等待maxWaitTime新建一个连接
         */
        private int maxCount = 500;

        /**
         * 最大空闲时间 单位/ms
         */
        private int maxIdleTime = 3600;

        /**
         * 最大等待时间 单位/ms
         */
        private long maxWaitTime = 1000;
    }
}
