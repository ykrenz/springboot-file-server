package com.github.ren.file.sdk.fdfs;

import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDFS extends StorageClient1 {

    public FastDFS() {
    }

    public FastDFS(TrackerServer trackerServer) {
        super(trackerServer);
    }

    public FastDFS(TrackerServer trackerServer, StorageServer storageServer) {
        super(trackerServer, storageServer);
    }
}
