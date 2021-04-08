package com.github.ren.file.clients;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * fastdfs实现类
 */
public class FastDfsFileClient extends AbstractServerClient implements FastDfsClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDfsFileClient.class);

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private AppendFileStorageClient appendFileStorageClient;

    @Resource
    private FdfsWebServer fdfsWebServer;

    @Override
    public String getWebServerUrl() {
        return fdfsWebServer.getWebServerUrl();
    }

    @Override
    public boolean isExist(String objectName) {
        String[] split = objectName.split("/");
        String group = split[0];
        String path = split[1];
        return fastFileStorageClient.queryFileInfo(group, path) == null;
    }

    @Override
    public String uploadFile(File file, String yourObjectName) {
        return this.getFullPath(this.uploadFileStorePath(file, yourObjectName));
    }

    @Override
    public String uploadFile(InputStream is, String yourObjectName) {
        return this.getFullPath(this.uploadFileStorePath(is, yourObjectName));
    }

    @Override
    public String uploadFile(byte[] content, String yourObjectName) {
        return this.getFullPath(this.uploadFileStorePath(content, yourObjectName));
    }

    @Override
    public String uploadPart(List<File> files, String yourObjectName) {
        return this.getFullPath(this.uploadPartStorePath(files, yourObjectName));
    }

    @Override
    public StorePath uploadFileStorePath(File file, String yourObjectName) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            return fastFileStorageClient.uploadFile(is, file.length(),
                    FilenameUtils.getExtension(yourObjectName), null);
        } catch (IOException e) {
            throw new FileIOException("local file load error", e);
        }
    }

    @Override
    public StorePath uploadFileStorePath(InputStream is, String yourObjectName) {
        try {
            return fastFileStorageClient.uploadFile(is, is.available(),
                    FilenameUtils.getExtension(yourObjectName), null);
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    @Override
    public StorePath uploadFileStorePath(byte[] content, String yourObjectName) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            return fastFileStorageClient.uploadFile(is, is.available(), FilenameUtils.getExtension(yourObjectName), null);
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    @Override
    public StorePath uploadFileStorePath(String url, String yourObjectName) {
        try (InputStream is = new URL(url).openStream()) {
            return fastFileStorageClient.uploadFile(is, is.available(), FilenameUtils.getExtension(yourObjectName), null);
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    @Override
    public StorePath uploadPartStorePath(List<File> files, String yourObjectName) {
        StorePath storePath = null;
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            try (FileInputStream in = FileUtils.openInputStream(file)) {
                if (i == 0) {
                    storePath = appendFileStorageClient.uploadAppenderFile(null, in,
                            file.length(), FilenameUtils.getExtension(yourObjectName));
                } else {
                    appendFileStorageClient.appendFile(storePath.getGroup(), storePath.getPath(),
                            in, file.length());
                }
            } catch (IOException e) {
                throw new FileIOException(e);
            }
        }
        return storePath;
    }

    @Override
    public StorePath uploadImageAndCrtThumbImage(File file, String yourObjectName) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            return fastFileStorageClient.uploadImageAndCrtThumbImage(is, file.length(), FilenameUtils.getExtension(yourObjectName), null);
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    @Override
    public StorePath uploadImageAndCrtThumbImage(InputStream is, String yourObjectName) {
        try {
            return fastFileStorageClient.uploadImageAndCrtThumbImage(is, is.available(), FilenameUtils.getExtension(yourObjectName), null);
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    @Override
    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
        return fastFileStorageClient.downloadFile(groupName, path, callback);
    }

    @Override
    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        return fastFileStorageClient.downloadFile(groupName, path, fileOffset, fileSize, callback);
    }

    @Override
    public void deleteFile(String groupName, String path) {
        fastFileStorageClient.deleteFile(groupName, path);
    }

    @Override
    public void deleteFile(StorePath storePath) {
        fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public String getFullPath(StorePath storePath) {
        return storePath.getFullPath();
    }

    @Override
    public String getPath(StorePath storePath) {
        return storePath.getPath();
    }

    @Override
    public String getGroup(StorePath storePath) {
        return storePath.getGroup();
    }
}
