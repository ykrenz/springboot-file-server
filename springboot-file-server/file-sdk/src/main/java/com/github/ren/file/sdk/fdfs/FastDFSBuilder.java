package com.github.ren.file.sdk.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description fastdfs文件客戶端构建类
 * @Author ren
 * @Since 1.0
 */
public class FastDFSBuilder {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSBuilder.class);

    private static final String CONFIG_FILE = "fdfs_client.conf";

    private static final String CONFIG_PROPERTIES = "fastdfs-client.properties";

    static {
        try {
            InputStream inputStream = FastDFSClient.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (inputStream != null) {
                //加载配置
                ClientGlobal.init(CONFIG_FILE);
            } else {
                ClientGlobal.initByProperties(CONFIG_PROPERTIES);
            }
        } catch (IOException | MyException e) {
            logger.error("fdfs client config load error " + e.getMessage());
            throw new IllegalStateException("fdfs_client config load error " + e.getMessage());
        }
    }

    public static FastDFS build() {
        try {
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //获取TrackerServer对象
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            //创建StorageServer对象
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //使用TrackerServer和StorageServer构造StorageClient对象
            return new FastDFS(trackerServer, storageServer);
        } catch (Exception e) {
            logger.error("getStorageClient error " + e.getMessage());
            throw new IllegalStateException("getStorageClient error", e);
        }
    }

}
