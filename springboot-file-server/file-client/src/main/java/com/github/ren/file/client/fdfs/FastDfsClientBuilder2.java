package com.github.ren.file.client.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
public class FastDfsClientBuilder2 implements FastDfsBuilder2 {

    private static final Logger logger = LoggerFactory.getLogger(FastDfsClientBuilder2.class);

    @Override
    public FastDfsStorageClient configFile() {
        return configFile(CONFIG_FILE);
    }

    @Override
    public FastDfsStorageClient propertiesFile() {
        return propertiesFile(PROPERTIES_FILE);
    }

    public static void main(String[] args) {
        new FastDfs();
    }
    @Override
    public FastDfsStorageClient configFile(String configFilePath) {
//        ClientGlobal.init(configFilePath);
        return new FastDfs();
    }

    @Override
    public FastDfsStorageClient propertiesFile(String propertiesFilePath) {
//        ClientGlobal.initByProperties(propertiesFilePath);
        return new FastDfs();
    }

    @Override
    public FastDfsStorageClient build(String trackerServers) {
//        ClientGlobal.initByProperties(getProperties(trackerServers, getClientConfiguration()));
        return new FastDfs(trackerServers);
    }

    @Override
    public FastDfsStorageClient build(FastDfsClientConfiguration clientConfiguration) {
        if (clientConfiguration == null) {
            throw new IllegalStateException("fastdfs client build error clientConfiguration is null");
        }
        String trackerServers = clientConfiguration.getTrackerServers();
        if (trackerServers == null) {
            throw new IllegalStateException("fastdfs client build error item \"tracker_server\" not found");
        }
        try {
            ClientGlobal.initByProperties(getProperties(trackerServers, clientConfiguration));
            logger.info("init fastdfs client success config: {} ", ClientGlobal.configInfo());
            return new FastDfs();
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
