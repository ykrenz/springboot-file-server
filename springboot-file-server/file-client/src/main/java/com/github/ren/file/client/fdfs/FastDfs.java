package com.github.ren.file.client.fdfs;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDfs implements FastDfsStorageClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDfs.class);

    private String group;
    protected TrackerServer trackerServer;
    protected StorageServer storageServer;

    public FastDfs(TrackerServer trackerServer, StorageServer storageServer) {
        this.trackerServer = trackerServer;
        this.storageServer = storageServer;
    }

    public FastDfs(String group, TrackerServer trackerServer, StorageServer storageServer) {
        this.group = group;
        this.trackerServer = trackerServer;
        this.storageServer = storageServer;
    }

    @Override
    public String getClientGroup() {
        return group;
    }

    @Override
    public TrackerServer getTrackerServer() {
        return trackerServer;
    }

    @Override
    public StorageServer getStorageServer() {
        return storageServer;
    }

    @Override
    public StorageClient1 getStorageClient() {
        return new StorageClient1(trackerServer, storageServer);
    }

    @Override
    public String uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        StorageClient1 storageClient = getStorageClient();
        try {
            return storageClient.upload_file1(group, IOUtils.toByteArray(inputStream), fileExtName, null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }
}
