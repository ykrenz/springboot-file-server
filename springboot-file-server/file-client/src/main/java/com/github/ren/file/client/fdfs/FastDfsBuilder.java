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
 * @Description fastdfs文件客戶端Builder
 * @Author ren
 * @Since 1.0
 */
public class FastDfsBuilder {

    private static final Logger logger = LoggerFactory.getLogger(FastDfsBuilder.class);

    public static final String CONFIG_FILE = "fdfs_client.conf";

    public static final String CONFIG_PROPERTIES = "fastdfs-client.properties";

    public FastDfsClient build(String trackerServers) {
        return this.build(null, trackerServers);
    }

    public FastDfsClient build(String group, String trackerServers) {
        try {
            List<InetSocketAddress> list = new ArrayList();
            String spr1 = ",";
            String spr2 = ":";
            String[] arr1 = trackerServers.trim().split(spr1);
            for (String addrStr : arr1) {
                String[] arr2 = addrStr.trim().split(spr2);
                String host = arr2[0].trim();
                int port = Integer.parseInt(arr2[1].trim());
                list.add(new InetSocketAddress(host, port));
            }
            InetSocketAddress[] trackerAddresses = list.toArray(new InetSocketAddress[list.size()]);
            TrackerGroup trackerGroup = new TrackerGroup(trackerAddresses);
            TrackerClient trackerClient = new TrackerClient(trackerGroup);
            TrackerServer trackerServer = trackerGroup.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            return new FastDfsClient(group, trackerServer, storageServer);
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error" + e.getMessage());
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }


    public static void initConfFile(String conf_filename) {
        try {
            ClientGlobal.init(conf_filename);
        } catch (IOException | MyException e) {
            logger.error("fastdfs client config load error " + e.getMessage());
            throw new IllegalStateException("fastdfs client config load error " + e.getMessage());
        }
    }

    public static void initPropsFile(String propsFile) {
        try {
            ClientGlobal.initByProperties(propsFile);
        } catch (IOException | MyException e) {
            logger.error("fastdfs client config load error " + e.getMessage());
            throw new IllegalStateException("fastdfs client config load error " + e.getMessage());
        }
    }

    public static void initProperties(Properties props) {
        try {
            ClientGlobal.initByProperties(props);
        } catch (IOException | MyException e) {
            logger.error("fastdfs client config load error " + e.getMessage());
            throw new IllegalStateException("fastdfs client config load error " + e.getMessage());
        }
    }

}
