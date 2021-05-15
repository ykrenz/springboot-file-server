package com.github.ren.file.sdk.fdfs;

import com.github.ren.file.sdk.AbstractFileClient;
import com.github.ren.file.sdk.ClientException;
import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.Util;
import com.github.ren.file.sdk.model.FdfsUploadResult;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.UploadPart;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDFSClient extends AbstractFileClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

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

    public StorageClient getStorageClient() {
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
    public FdfsUploadResult upload(File file, String yourObjectName) {
        try {
            String[] result = getStorageClient().upload_file(file.getAbsolutePath(), FilenameUtils.getExtension(file.getName()), null);
            String group = result[0];
            String path = result[1];
            return new FdfsUploadResult(group, path);
        } catch (IOException | MyException e) {
            throw new ClientException(e.getMessage());
        }
    }

    @Override
    public FdfsUploadResult upload(InputStream is, String yourObjectName) {
        try {
            String[] result = getStorageClient().upload_file(IOUtils.toByteArray(is), FilenameUtils.getExtension(yourObjectName), null);
            String group = result[0];
            String path = result[1];
            return new FdfsUploadResult(group, path);
        } catch (IOException | MyException e) {
            throw new ClientException(e.getMessage());
        }
    }

    @Override
    public FdfsUploadResult upload(byte[] content, String yourObjectName) {
        try {
            String[] result = getStorageClient().upload_file(content, FilenameUtils.getExtension(yourObjectName), null);
            String group = result[0];
            String path = result[1];
            return new FdfsUploadResult(group, path);
        } catch (IOException | MyException e) {
            throw new ClientException(e.getMessage());
        }
    }

    @Override
    public FdfsUploadResult upload(String url, String yourObjectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, yourObjectName);
        } catch (IOException e) {
            throw new FileIOException("fdfs upload url file error", e);
        }
    }

    @Override
    public String initiateMultipartUpload(String yourObjectName) {
        return partStore.initiateMultipartUpload(yourObjectName);
    }

    @Override
    public List<PartInfo> listParts(String uploadId, String yourObjectName) {
        return partStore.listParts(uploadId, yourObjectName);
    }

    @Override
    protected String uploadPartFile(UploadPart part) {
        return partStore.uploadPart(part);
    }

    @Override
    protected CompleteMultipart merge(String uploadId, String yourObjectName) {
        try {
            List<PartInfo> partInfos = listParts(uploadId, yourObjectName);
            partInfos.sort(Comparator.comparing(PartInfo::getPartNumber));

            long sum = partInfos.stream().mapToLong(PartInfo::getPartSize).sum();
            String[] result = getStorageClient().upload_appender_file("".getBytes(StandardCharsets.UTF_8), FilenameUtils.getName(yourObjectName), null);
            String group = result[0];
            String path = result[1];
            getStorageClient().truncate_file(group, path, sum);

            List<ModifyPart> modifyParts = new ArrayList<>(partInfos.size());
            long file_offset = 0;
            for (PartInfo partInfo : partInfos) {
                UploadPart uploadPart = partStore.getUploadPart(uploadId, yourObjectName, partInfo.getPartNumber());
                ModifyPart modifyPart = new ModifyPart();
                modifyPart.setGroup_name(group);
                modifyPart.setAppender_filename(path);
                modifyPart.setFile_buff(IOUtils.toByteArray(uploadPart.getInputStream()));
                modifyPart.setFile_offset(file_offset);
                file_offset += partInfo.getPartSize();
                modifyParts.add(modifyPart);
            }

            for (ModifyPart modifyPart : modifyParts) {
                getStorageClient().modify_file(modifyPart.getGroup_name(), modifyPart.getAppender_filename(),
                        modifyPart.getFile_offset(), modifyPart.getFile_buff());
            }
//            ExecutorService executorService = Executors.newFixedThreadPool(10);
//            Callable callable = new
//                    executorService.invokeAll()
            return new CompleteMultipart("", group + Util.SLASH + path);
        } catch (MyException | IOException e) {
            throw new ClientException(e.getMessage());
        }

    }

    @Data
    class ModifyPart {
        private String group_name;
        private String appender_filename;
        private long file_offset;
        private byte[] file_buff;
    }
}
