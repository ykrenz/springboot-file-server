package com.github.ren.file.client.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDfs implements FastDfsStorageClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDfs.class);

    private TrackerClient trackerClient;
    private TrackerServer trackerServer;
    private StorageServer storageServer;

    public FastDfs() {
        if (ClientGlobal.g_tracker_group == null) {
            throw new IllegalStateException("ClientGlobal config not init g_tracker_group is null");
        }
        try {
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getTrackerServer();
            storageServer = trackerClient.getStoreStorage(trackerServer);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    public FastDfs(String trackerServers) {
        if (trackerServers == null || trackerServers.length() == 0) {
            throw new IllegalArgumentException("fastdfs client build error argument trackerServers is null");
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
            trackerClient = new TrackerClient(trackerGroup);
            trackerServer = trackerGroup.getTrackerServer();
            storageServer = trackerClient.getStoreStorage(trackerServer);
            logger.info("init fastdfs client success config: {} ", ClientGlobal.configInfo());
        } catch (IOException | MyException e) {
            logger.error("fastdfs client build error", e);
            throw new IllegalStateException("fastdfs client build error " + e.getMessage());
        }
    }

    @Override
    public TrackerClient getTrackerClient() {
        return trackerClient;
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
    public String uploadFile(File file) {
        return uploadFile(file, null);
    }

    @Override
    public String uploadFile(File file, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(file.getAbsolutePath(), FilenameUtils.getExtension(file.getName()), metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(String groupName, File file) {
        return uploadFile(groupName, file, null);
    }

    @Override
    public String uploadFile(String groupName, File file, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(groupName, file.getAbsolutePath(), FilenameUtils.getExtension(file.getName()), metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileExtName) {
        return uploadFile(inputStream, fileExtName, null);
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileExtName, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(IOUtils.toByteArray(inputStream), fileExtName, metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(String groupName, InputStream inputStream, String fileExtName) {
        return uploadFile(groupName, inputStream, fileExtName, null);
    }

    @Override
    public String uploadFile(String groupName, InputStream inputStream, String fileExtName, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(groupName, IOUtils.toByteArray(inputStream), fileExtName, metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadFile(String masterFilename, String prefixName, InputStream inputStream, String fileExtName) {
        return uploadFile(masterFilename, prefixName, inputStream, fileExtName, null);
    }

    @Override
    public String uploadFile(String masterFilename, String prefixName, InputStream inputStream, String fileExtName, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_file1(masterFilename, prefixName, IOUtils.toByteArray(inputStream), fileExtName, metaData);
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

    @Override
    public boolean deleteFile(String groupName, String path) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.delete_file(groupName, path) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public NameValuePair[] getMetadata(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.get_metadata1(filePath);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public NameValuePair[] getMetadata(String groupName, String path) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.get_metadata(groupName, path);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean overwriteMetadata(String filePath, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.set_metadata1(filePath, metaData, ProtoCommon.STORAGE_SET_METADATA_FLAG_OVERWRITE) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean overwriteMetadata(String groupName, String path, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.set_metadata(groupName, path, metaData, ProtoCommon.STORAGE_SET_METADATA_FLAG_OVERWRITE) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean mergeMetadata(String filePath, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.set_metadata1(filePath, metaData, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean mergeMetadata(String groupName, String path, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.set_metadata(groupName, path, metaData, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public FileInfo queryFileInfo(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.query_file_info1(filePath);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }


    @Override
    public FileInfo queryFileInfo(String groupName, String path) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.query_file_info(groupName, path);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadLocalFile(String filePath, String localFile) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file1(filePath, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadLocalFile(String groupName, String path, String localFile) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file(groupName, path, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadLocalFile(String groupName, String path, long offset, long size, String localFile) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file(groupName, path, offset, size, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file1(filePath);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String filePath, long offset, long size) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file1(filePath, offset, size);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String groupName, String path) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file(groupName, path);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String groupName, String path, long offset, long size) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file(groupName, path, offset, size);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadFile(String filePath, DownloadCallback callback) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file1(filePath, callback) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadFile(String filePath, long offset, long size, DownloadCallback callback) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file1(filePath, offset, size, callback) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadFile(String groupName, String path, DownloadCallback callback) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file(groupName, path, callback) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean downloadFile(String groupName, String path, long offset, long size, DownloadCallback callback) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.download_file(groupName, path, offset, size, callback) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

}
