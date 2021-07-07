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

    private TrackerGroup trackerGroup;

    private TrackerClient trackerClient;

    private String groupName;

    /**
     * 构建简单客户端
     *
     * @param trackerServers
     */
    public FastDfs(String trackerServers) {
        assertParameterNotNull(trackerServers, "FastDfs client init error argument trackerServers is null");
        initTrackerGroup(trackerServers);
        logger.info("FastDfs client init success trackerServers: {} ", trackerServers);
    }

    /**
     * 使用trackerServers构建带有groupName的客户端
     *
     * @param trackerServers
     */
    public FastDfs(String trackerServers, String groupName) {
        assertParameterNotNull(trackerServers, "FastDfs client init error argument trackerServers is null");
        initTrackerGroup(trackerServers);
        this.groupName = groupName;
        logger.info("FastDfs client init success trackerServers: {} ", trackerServers);
    }

    private void assertParameterNotNull(Object parameterValue, String errorMessage) {
        if (parameterValue == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void initTrackerGroup(String trackerServers) {
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
        this.trackerGroup = new TrackerGroup(trackerAddresses);
        this.trackerClient = new TrackerClient(trackerGroup);
    }

    private StorageClient1 getStorageClient() throws MyException, IOException {
        //从trackerGroup轮询获取到一个tracker
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        if (trackerServer == null) {
            throw new FastDfsException("未找到可用的tracker");
        }
        StorageServer storageServer = null;
        if (groupName != null) {
            storageServer = trackerClient.getStoreStorage(trackerServer, groupName);
            if (storageServer == null) {
                logger.warn("未找到tracker{} 对应的组 {} 会导致上传到其他组", trackerServer.getInetSocketAddress(), groupName);
                storageServer = trackerClient.getStoreStorage(trackerServer);
            }
        }
        return new StorageClient1(trackerServer, storageServer);
    }

    @Override
    public TrackerGroup trackerGroup() {
        return trackerGroup;
    }

    @Override
    public TrackerClient trackerClient() {
        return trackerClient;
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
