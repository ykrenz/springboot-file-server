package com.github.ren.file.sdk.fdfs;

import com.github.ren.file.sdk.AbstractFileClient;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.UploadPart;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDFSClient extends AbstractFileClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    static {
        try {
            //加载配置
            ClientGlobal.init("fdfs_client.conf");
            ClientGlobal.initByProperties("fastdfs-client.properties");
        } catch (IOException | MyException e) {
            logger.error("fdfs_client config load error " + e.getMessage());
            throw new IllegalStateException("fdfs_client config load error " + e.getMessage());
        }
    }

    private StorageClient getStorageClient() {
        try {
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //获取TrackerServer对象
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            //创建StorageServer对象
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //使用TrackerServer和StorageServer构造StorageClient对象
            return new StorageClient(trackerServer, storageServer);
        } catch (Exception e) {
            logger.error("getStorageClient error " + e.getMessage());
            throw new IllegalStateException("getStorageClient error", e);
        }
    }

    @Override
    protected String uploadPartFile(UploadPart part) {
        return null;
    }

    @Override
    protected CompleteMultipart merge(String uploadId, String yourObjectName) {
        return null;
    }

    @Override
    public String upload(File file, String yourObjectName) {
        String[] strings = null;
        try {
            strings = getStorageClient().upload_file(file.getAbsolutePath(), FilenameUtils.getExtension(file.getName()), null);
            return Arrays.toString(strings);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String upload(InputStream is, String yourObjectName) {
        return null;
    }

    @Override
    public String upload(byte[] content, String yourObjectName) {
        return null;
    }

    @Override
    public String upload(String url, String yourObjectName) {
        return null;
    }

    @Override
    public String initiateMultipartUpload(String yourObjectName) {
        return null;
    }

    @Override
    public List<PartInfo> listParts(String uploadId, String yourObjectName) {
        return null;
    }
}
