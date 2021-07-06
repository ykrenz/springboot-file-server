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
        try {
            ClientGlobal.init(configFilePath);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            FastDfsClient fastDfsClient = new FastDfsClient(trackerServer, storageServer);
            logger.info("init fastdfs client success config: {} ", ClientGlobal.configInfo());
            return fastDfsClient;
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error", e);
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }

    @Override
    public FastDfsClient propertiesFile(String propertiesFilePath) {
        try {
            ClientGlobal.initByProperties(propertiesFilePath);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            FastDfsClient fastDfsClient = new FastDfsClient(trackerServer, storageServer);
            logger.info("init fastdfs client success config: {} ", ClientGlobal.configInfo());
            return fastDfsClient;
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error", e);
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }

    @Override
    public FastDfsClient build(String trackerServers) {
        FastDfsClientConfiguration clientConfiguration = getClientConfiguration();
        clientConfiguration.setTrackerServers(trackerServers);
        return build(clientConfiguration);
    }

    @Override
    public FastDfsClient build(FastDfsClientConfiguration clientConfiguration) {
        if (clientConfiguration == null) {
            throw new IllegalStateException("fastdfs client build error clientConfiguration is null");
        }
        String trackerServers = clientConfiguration.getTrackerServers();
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
            FastDfsClient fastDfsClient = new FastDfsClient(trackerServer, storageServer);
            logger.info("init fastdfs client success config: {} ", ClientGlobal.configInfo());
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
            props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, trackerServers);
        }
        int connectTimeoutSeconds = clientConfiguration.getConnectTimeoutSeconds();
        if (connectTimeoutSeconds > 0) {
            props.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, String.valueOf(connectTimeoutSeconds));
        }
        long networkTimeoutSeconds = clientConfiguration.getNetworkTimeoutSeconds();
        if (networkTimeoutSeconds > 0) {
            props.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, String.valueOf(networkTimeoutSeconds));
        }
        String charset = clientConfiguration.getCharset();
        if (charset != null) {
            props.put(ClientGlobal.PROP_KEY_CHARSET, charset);
        }
        boolean httpAntiStealToken = clientConfiguration.isHttpAntiStealToken();
        props.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, String.valueOf(httpAntiStealToken));
        String httpSecretKey = clientConfiguration.getHttpSecretKey();
        if (httpSecretKey != null) {
            props.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, httpSecretKey);
        }
        int httpTrackerHttpPort = clientConfiguration.getHttpTrackerHttpPort();
        if (httpTrackerHttpPort > 0) {
            props.put(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT, String.valueOf(httpTrackerHttpPort));
        }
        FastDfsClientConfiguration.Pool pool = clientConfiguration.getPool();
        if (pool != null) {
            props.put(ClientGlobal.PROP_KEY_CONNECTION_POOL_ENABLED, String.valueOf(pool.isEnabled()));
            int maxCount = pool.getMaxCount();
            if (maxCount > 0) {
                props.put(ClientGlobal.PROP_KEY_CONNECTION_POOL_MAX_COUNT_PER_ENTRY, String.valueOf(maxCount));
            }
            int maxIdleTime = pool.getMaxIdleTime();

            if (maxIdleTime > 0) {
                props.put(ClientGlobal.PROP_KEY_CONNECTION_POOL_MAX_IDLE_TIME, String.valueOf(maxIdleTime));
            }
            long maxWaitTime = pool.getMaxWaitTime();
            if (maxWaitTime > 0) {
                props.put(ClientGlobal.PROP_KEY_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS, String.valueOf(maxWaitTime));
            }
        }
        return props;
    }
}
