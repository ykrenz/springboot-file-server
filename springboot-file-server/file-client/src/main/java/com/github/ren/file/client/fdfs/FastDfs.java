package com.github.ren.file.client.fdfs;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDfs implements FastDfsStorageClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDfs.class);

    protected TrackerServer trackerServer;
    protected StorageServer storageServer;

    public FastDfs(TrackerServer trackerServer, StorageServer storageServer) {
        this.trackerServer = trackerServer;
        this.storageServer = storageServer;
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
    public String uploadFile(String groupName, File file) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(groupName, file.getAbsolutePath(), FilenameUtils.getExtension(file.getName()), null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(String groupName, InputStream inputStream, String fileExtName) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(groupName, IOUtils.toByteArray(inputStream), fileExtName, null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileExtName, Set<MetaData> metaDataSet) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(IOUtils.toByteArray(inputStream), fileExtName, null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadSlaveFile(String masterFilename, String prefixName, InputStream inputStream, String fileExtName) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(masterFilename, prefixName, IOUtils.toByteArray(inputStream), fileExtName, null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.delete_file1(filePath) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

//    @Override
//    public Set<MetaData> getMetadata(String groupName, String path) {
//        return null;
//    }
//
//    @Override
//    public void overwriteMetadata(String groupName, String path, Set<MetaData> metaDataSet) {
//
//    }
//
//    @Override
//    public void mergeMetadata(String groupName, String path, Set<MetaData> metaDataSet) {
//
//    }

//    @Override
//    public FileInfo queryFileInfo(String groupName, String path) {
//        return null;
//    }

    @Override
    public boolean deleteFile(String groupName, String path) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.delete_file(groupName, path) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

//    @Override
//    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
//        return null;
//    }
//
//    @Override
//    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback) {
//        return null;
//    }
}
