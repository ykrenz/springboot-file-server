package com.github.ren.file.client.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description FastDfsClientBuilder 用于构建FastDfsClient
 * @Author ren
 * @Since 1.0
 */
public class FastDfsClientBuilder implements FastDfsBuilder {

    private static final Logger logger = LoggerFactory.getLogger(FastDfsClientBuilder.class);

    @Override
    public FastDfsClient configFile() {
        return configFile(CONFIG_FILE);
    }

    @Override
    public FastDfsClient propertiesFile() {
        return propertiesFile(PROPERTIES_FILE);
    }

    @Override
    public FastDfsClient configFile(String configFilePath) {
        return configFile(null, configFilePath);
    }

    @Override
    public FastDfsClient propertiesFile(String propertiesFilePath) {
        return propertiesFile(null, propertiesFilePath);
    }

    @Override
    public FastDfsClient configFile(String group, String configFilePath) {
        try {
            ClientGlobal.init(configFilePath);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            FastDfsClient fastDfsClient = new FastDfsClient(group, trackerServer, storageServer);
            logger.info("init fastdfs client success group: {} conifg: {} ", group, ClientGlobal.configInfo());
            return fastDfsClient;
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error", e);
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }

    @Override
    public FastDfsClient propertiesFile(String group, String propertiesFilePath) {
        try {
            ClientGlobal.initByProperties(propertiesFilePath);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            FastDfsClient fastDfsClient = new FastDfsClient(group, trackerServer, storageServer);
            logger.info("init fastdfs client success group: {} conifg: {} ", group, ClientGlobal.configInfo());
            return fastDfsClient;
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error", e);
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }

    @Override
    public FastDfsClient build(String trackerServers) {
        return build(null, trackerServers, getClientConfiguration());
    }

    @Override
    public FastDfsClient build(String group, String trackerServers) {
        return build(group, trackerServers, getClientConfiguration());
    }

    @Override
    public FastDfsClient build(String trackerServers, FastDfsClientConfiguration clientConfiguration) {
        return build(null, trackerServers, clientConfiguration);
    }

    @Override
    public FastDfsClient build(String group, String trackerServers, FastDfsClientConfiguration clientConfiguration) {
        if (trackerServers == null) {
            throw new IllegalStateException("fastdfs client build error item \"tracker_server\" not found");
        }
        try {
            List<InetSocketAddress> list = new ArrayList<>();
            String spr1 = ",";
            String spr2 = ":";
            String[] arr1 = trackerServers.trim().split(spr1);
            for (String addrStr : arr1) {
                String[] arr2 = addrStr.trim().split(spr2);
                String host = arr2[0].trim();
                int port = Integer.parseInt(arr2[1].trim());
                list.add(new InetSocketAddress(host, port));
            }
            InetSocketAddress[] trackerAddresses = list.toArray(new InetSocketAddress[0]);
            TrackerGroup trackerGroup = new TrackerGroup(trackerAddresses);
            TrackerClient trackerClient = new TrackerClient(trackerGroup);
            TrackerServer trackerServer = trackerGroup.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            ClientGlobal.initByProperties(getProperties(trackerServers, clientConfiguration));
            FastDfsClient fastDfsClient = new FastDfsClient(group, trackerServer, storageServer);
            logger.info("init fastdfs client success group: {} conifg: {} ", group, ClientGlobal.configInfo());
            return fastDfsClient;
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error", e);
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }

    private FastDfsClientConfiguration getClientConfiguration() {
        return new FastDfsClientConfiguration();
    }


    private Properties getProperties(String trackerServers, FastDfsClientConfiguration clientConfiguration) {
        Properties props = new Properties();
        if (trackerServers != null) {
            props.put("fastdfs.tracker_servers", trackerServers);
        }
        int connectTimeoutSeconds = clientConfiguration.getConnectTimeoutSeconds();
        if (connectTimeoutSeconds > 0) {
            props.put("fastdfs.connect_timeout_in_seconds", connectTimeoutSeconds);
        }
        long networkTimeoutSeconds = clientConfiguration.getNetworkTimeoutSeconds();
        if (networkTimeoutSeconds > 0) {
            props.put("fastdfs.network_timeout_in_seconds", networkTimeoutSeconds);
        }
        String charset = clientConfiguration.getCharset();
        if (charset != null) {
            props.put("fastdfs.charset", charset);
        }
        boolean httpAntiStealToken = clientConfiguration.isHttpAntiStealToken();
        props.put("fastdfs.http_anti_steal_token", httpAntiStealToken);
        String httpSecretKey = clientConfiguration.getHttpSecretKey();
        if (httpSecretKey != null) {
            props.put("fastdfs.http_secret_key", httpSecretKey);
        }
        int httpTrackerHttpPort = clientConfiguration.getHttpTrackerHttpPort();
        if (httpTrackerHttpPort > 0) {
            props.put("fastdfs.http_tracker_http_port", httpTrackerHttpPort);
        }
        FastDfsClientConfiguration.Pool pool = clientConfiguration.getPool();
        if (pool != null) {
            props.put("fastdfs.connection_pool.enabled", pool.isEnabled());
            int maxCount = pool.getMaxCount();
            if (maxCount > 0) {
                props.put("fastdfs.connection_pool.max_count_per_entry", maxCount);
            }
            int maxIdleTime = pool.getMaxIdleTime();

            if (maxIdleTime > 0) {
                props.put("fastdfs.connection_pool.max_idle_time", maxIdleTime);
            }
            long maxWaitTime = pool.getMaxWaitTime();
            if (maxWaitTime > 0) {
                props.put("fastdfs.connection_pool.max_wait_time_in_ms", maxWaitTime);
            }
        }
        return props;
    }
}
