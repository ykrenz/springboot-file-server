package com.github.ren.file.client.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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

    @Override
    public String uploadAppenderFile(String localFile) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_appender_file1(localFile,
                    FilenameUtils.getExtension(new File(localFile).getName()), null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadAppenderFile(String localFile, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_appender_file1(localFile,
                    FilenameUtils.getExtension(new File(localFile).getName()), metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadAppenderFile(InputStream inputStream, String fileExtName) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_appender_file1(IOUtils.toByteArray(inputStream), fileExtName, null);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadAppenderFile(InputStream inputStream, String fileExtName, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_appender_file1(IOUtils.toByteArray(inputStream), fileExtName, metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String uploadAppenderFile(String groupName, InputStream inputStream, String fileExtName, NameValuePair[] metaData) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.upload_appender_file1(groupName, IOUtils.toByteArray(inputStream), fileExtName, metaData);
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean appendFile(String filePath, String localFile) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.append_file1(filePath, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean appendFile(String groupName, String path, String localFile) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.append_file(groupName, path, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean appendFile(String filePath, InputStream inputStream) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.append_file1(filePath, IOUtils.toByteArray(inputStream)) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean appendFile(String groupName, String path, InputStream inputStream) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.append_file(groupName, path, IOUtils.toByteArray(inputStream)) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean appendFile(String groupName, String path, InputStream inputStream, int offset, int length) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.append_file(groupName, path,
                    IOUtils.toByteArray(inputStream), offset, length) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean modifyFile(String filePath, String localFile, long offset) {
        try {
            StorageClient1 storageClient = getStorageClient();
//            if (new File(localFile).length() == 0) {
//                return true;
//            }
            return storageClient.modify_file1(filePath, offset, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean modifyFile(String filePath, InputStream inputStream, long offset) {
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
//            if (bytes.length == 0) {
//                return true;
//            }
            StorageClient1 storageClient = getStorageClient();
            return storageClient.modify_file1(filePath, offset, bytes) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean modifyFile(String groupName, String path, String localFile, long offset) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.modify_file(groupName, path, offset, localFile) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean modifyFile(String groupName, String path, InputStream inputStream, long offset) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.modify_file(groupName, path, offset, IOUtils.toByteArray(inputStream)) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean truncateFile(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.truncate_file1(filePath) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean truncateFile(String filePath, long truncatedFileSize) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.truncate_file1(filePath, truncatedFileSize) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean truncateFile(String groupName, String path) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.truncate_file(groupName, path) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public boolean truncateFile(String groupName, String path, long truncatedFileSize) {
        try {
            StorageClient1 storageClient = getStorageClient();
            return storageClient.truncate_file(groupName, path, truncatedFileSize) == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException(e.getMessage());
        }
    }

    @Override
    public String initiateMultipartUpload(long fileSize, long partSize, String fileExtName) {
        try {
            if (fileSize <= 0) {
                throw new IllegalArgumentException("fileSize 必须大于0");
            }
            if (partSize <= 0) {
                throw new IllegalArgumentException("fileSize 必须大于0");
            }
            StorageClient1 storageClient = getStorageClient();
            //初始化 upload append文件
            NameValuePair[] metadata = new NameValuePair[]{
                    new NameValuePair("fileSize", String.valueOf(fileSize)),
                    new NameValuePair("partSize", String.valueOf(partSize))};
            String filePath = storageClient.upload_appender_file1(new byte[]{}, fileExtName, metadata);
            storageClient.truncate_file1(filePath, fileSize);
            return filePath;
        } catch (IOException | MyException e) {
            throw new FastDfsException("init part upload error", e);
        }
    }

    /**
     * 获取最大分片数量
     *
     * @param filesize
     * @param partSize
     * @return
     */
    private int getMaxPartCount(long filesize, long partSize) {
        return Math.max(1, (int) Math.ceil(filesize / (float) partSize));
    }

    private static final String Multipart = "Multipart-";

    @Override
    public FastPart uploadPart(String filePath, int partNumber, String localFile) {
        try {
            return uploadPart(filePath, partNumber, new FileInputStream(localFile));
        } catch (FileNotFoundException e) {
            throw new FastDfsException("FileNotFoundException", e);
        }
    }

    @Override
    public FastPart uploadPart(String filePath, int partNumber, InputStream inputStream) {
        try {
            if (partNumber <= 0) {
                throw new IllegalArgumentException("partNumber 必须大于0");
            }
            StorageClient1 storageClient = getStorageClient();
            NameValuePair[] metadata = storageClient.get_metadata1(filePath);
            long fileSize = 0;
            long partSize = 0;
            for (NameValuePair meta : metadata) {
                if ("fileSize".equals(meta.getName())) {
                    fileSize = Long.parseLong(meta.getValue());
                    break;
                }
            }
            for (NameValuePair meta : metadata) {
                if ("partSize".equals(meta.getName())) {
                    partSize = Long.parseLong(meta.getValue());
                    break;
                }
            }
            if (partSize <= 0) {
                throw new IllegalArgumentException("partSize 必须大于0");
            }
            byte[] bytes = IOUtils.toByteArray(inputStream);
            long currentSize = bytes.length;
            int maxPartCount = getMaxPartCount(fileSize, partSize);
            if (partNumber > maxPartCount) {
                throw new FastDfsException("partNumber is to large please check your part");
            }
            //最后一个分片
            if (currentSize != partSize) {
                if (partNumber != maxPartCount && partNumber != maxPartCount - 1) {
                    throw new FastDfsException("part size error please check your part");
                }
            }
            long offset = 0;
            if (partNumber != 1) {
                offset = (partNumber - 1) * partSize;
            }
            if (offset + currentSize > fileSize) {
                throw new FastDfsException("part size error please check your part");
            }
            storageClient.modify_file1(filePath, offset, bytes);
            NameValuePair[] metaList = new NameValuePair[]{
                    new NameValuePair(Multipart + partNumber, String.valueOf(partNumber))
            };
            storageClient.set_metadata1(filePath, metaList, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);

            FastPart fastPart = new FastPart();
            fastPart.setPartNumber(partNumber);
            fastPart.setPartSize(currentSize);
            return fastPart;
        } catch (IOException | MyException e) {
            throw new FastDfsException("upload part error", e);
        }
    }

    @Override
    public String completeMultipartUpload(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            String newPath = storageClient.regenerate_appender_filename1(filePath);
            storageClient.set_metadata1(newPath, null, ProtoCommon.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            return newPath;
        } catch (MyException | IOException e) {
            throw new FastDfsException("complete part error", e);
        }
    }

    @Override
    public List<FastPart> listParts(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            NameValuePair[] metadata = storageClient.get_metadata1(filePath);
            long partSize = 0;
            for (NameValuePair meta : metadata) {
                if ("partSize".equals(meta.getName())) {
                    partSize = Long.parseLong(meta.getValue());
                    break;
                }
            }
            List<FastPart> fastParts = new ArrayList<>(metadata.length - 2);
            for (NameValuePair meta : metadata) {
                if (meta.getName().startsWith(Multipart)) {
                    FastPart fastPart = new FastPart();
                    fastPart.setPartNumber(Integer.parseInt(meta.getValue()));
                    fastPart.setPartSize(partSize);
                    fastParts.add(fastPart);
                }
            }
            return fastParts;
        } catch (MyException | IOException e) {
            throw new FastDfsException("listParts error", e);
        }
    }

    @Override
    public boolean abortMultipartUpload(String filePath) {
        try {
            StorageClient1 storageClient = getStorageClient();
            NameValuePair[] metadata = storageClient.get_metadata1(filePath);
            if (metadata == null) {
                throw new FastDfsException("abort upload error maybe is complete");
            }
            return storageClient.delete_file1(filePath) == 0;
        } catch (MyException | IOException e) {
            throw new FastDfsException("abort part error", e);
        }
    }

}
