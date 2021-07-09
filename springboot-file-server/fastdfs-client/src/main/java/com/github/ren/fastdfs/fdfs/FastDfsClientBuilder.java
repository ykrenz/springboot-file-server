package com.github.ren.fastdfs.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
public class FastDfsClientBuilder implements FastDfsBuilder {

    private static final Logger logger = LoggerFactory.getLogger(FastDfsClientBuilder.class);

    @Override
    public FastDfsStorageClient configFile() {
        return configFile(CONFIG_FILE);
    }

    @Override
    public FastDfsStorageClient propertiesFile() {
        return propertiesFile(PROPERTIES_FILE);
    }

    @Override
    public FastDfsStorageClient configFile(String configFilePath) {
        try {
            ClientGlobal.init(configFilePath);
            return new FastDfs(getGlobalTrackerServers());
        } catch (IOException | MyException e) {
            throw new FastDfsException("FastDfs client build error " + e.getMessage());
        }
    }

    @Override
    public FastDfsStorageClient propertiesFile(String propertiesFilePath) {
        try {
            ClientGlobal.initByProperties(propertiesFilePath);
            return new FastDfs(getGlobalTrackerServers());
        } catch (IOException | MyException e) {
            throw new FastDfsException("FastDfs client build error " + e.getMessage());
        }
    }

    private String getGlobalTrackerServers() {
        StringBuilder trackerServers = new StringBuilder();
        if (ClientGlobal.g_tracker_group != null) {
            InetSocketAddress[] trackerAddresses = ClientGlobal.g_tracker_group.tracker_servers;
            for (InetSocketAddress inetSocketAddress : trackerAddresses) {
                if (trackerServers.length() > 0) {
                    trackerServers.append(",");
                }
                trackerServers.append(inetSocketAddress.toString().substring(1));
            }
        }
        return trackerServers.toString();
    }

    @Override
    public FastDfsStorageClient build(String trackerServers) {
        return new FastDfs(trackerServers);
    }

    @Override
    public FastDfsStorageClient build(String trackerServers, String groupName) {
        return new FastDfs(trackerServers, groupName);
    }

    @Override
    public FastDfsStorageClient build(String trackerServers, FastDfsClientConfiguration clientConfiguration) {
        try {
            Properties props = getProperties(clientConfiguration);
            if (trackerServers != null) {
                props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, trackerServers);
            }
            ClientGlobal.initByProperties(props);
            return new FastDfs(trackerServers);
        } catch (IOException | MyException e) {
            throw new FastDfsException("FastDfs client build error " + e.getMessage());
        }
    }

    @Override
    public FastDfsStorageClient build(String trackerServers, String groupName, FastDfsClientConfiguration clientConfiguration) {
        try {
            Properties props = getProperties(clientConfiguration);
            if (trackerServers != null) {
                props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, trackerServers);
            }
            ClientGlobal.initByProperties(props);
            return new FastDfs(trackerServers, groupName);
        } catch (IOException | MyException e) {
            throw new FastDfsException("FastDfs client build error " + e.getMessage());
        }
    }

    private Properties getProperties(FastDfsClientConfiguration clientConfiguration) {
        Properties props = new Properties();
        if (clientConfiguration == null) {
            return props;
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
