package com.github.ren.file.sdk.fdfs;

import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.UploadClient;
import com.github.ren.file.sdk.part.*;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.domain.upload.FastFile;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.DefaultAppendFileStorageClient;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @Description fastdfs文件客戶端适配器
 * @Author ren
 * @Since 1.0
 */
public class FdfsClient implements FastFileStorageClient, AppendFileStorageClient, UploadClient, FdfsUploadPartClient {

    private static final Logger logger = LoggerFactory.getLogger(FdfsClient.class);

    private final FastFileStorageClient fastFileStorageClient;

    private final AppendFileStorageClient appendFileStorageClient;

    private final UploadPartClient uploadPartClient;

    public FdfsClient(DefaultFastFileStorageClient fastFileStorageClient,
                      DefaultAppendFileStorageClient appendFileStorageClient,
                      UploadPartClient uploadPartClient) {
        this.fastFileStorageClient = fastFileStorageClient;
        this.appendFileStorageClient = appendFileStorageClient;
        this.uploadPartClient = uploadPartClient;
    }

    @Override
    public StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        return appendFileStorageClient.uploadAppenderFile(groupName, inputStream, fileSize, fileExtName);
    }

    @Override
    public void appendFile(String groupName, String path, InputStream inputStream, long fileSize) {
        appendFileStorageClient.appendFile(groupName, path, inputStream, fileSize);
    }

    @Override
    public void modifyFile(String groupName, String path, InputStream inputStream, long fileSize, long fileOffset) {
        appendFileStorageClient.modifyFile(groupName, path, inputStream, fileSize, fileOffset);
    }

    @Override
    public void truncateFile(String groupName, String path, long truncatedFileSize) {
        appendFileStorageClient.truncateFile(groupName, path, truncatedFileSize);
    }

    @Override
    public void truncateFile(String groupName, String path) {
        appendFileStorageClient.truncateFile(groupName, path);
    }

    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        return fastFileStorageClient.uploadFile(inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadImageAndCrtThumbImage(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        return fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadImage(FastImageFile fastImageFile) {
        return fastFileStorageClient.uploadImage(fastImageFile);
    }

    @Override
    public StorePath uploadFile(FastFile fastFile) {
        return fastFileStorageClient.uploadFile(fastFile);
    }

    @Override
    public void deleteFile(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }

    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        return fastFileStorageClient.uploadFile(groupName, inputStream, fileSize, fileExtName);
    }

    @Override
    public StorePath uploadSlaveFile(String groupName, String masterFilename, InputStream inputStream, long fileSize, String prefixName, String fileExtName) {
        return fastFileStorageClient.uploadSlaveFile(groupName, masterFilename, inputStream, fileSize, prefixName, fileExtName);
    }

    @Override
    public Set<MetaData> getMetadata(String groupName, String path) {
        return fastFileStorageClient.getMetadata(groupName, path);
    }

    @Override
    public void overwriteMetadata(String groupName, String path, Set<MetaData> metaDataSet) {
        fastFileStorageClient.overwriteMetadata(groupName, path, metaDataSet);
    }

    @Override
    public void mergeMetadata(String groupName, String path, Set<MetaData> metaDataSet) {
        fastFileStorageClient.mergeMetadata(groupName, path, metaDataSet);
    }

    @Override
    public FileInfo queryFileInfo(String groupName, String path) {
        return fastFileStorageClient.queryFileInfo(groupName, path);
    }

    @Override
    public void deleteFile(String groupName, String path) {
        fastFileStorageClient.deleteFile(groupName, path);
    }

    @Override
    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
        return fastFileStorageClient.downloadFile(groupName, path, callback);
    }

    @Override
    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        return fastFileStorageClient.downloadFile(groupName, path, fileOffset, fileSize, callback);
    }

    public String getFileExtName(String yourObjectName) {
        return FilenameUtils.getExtension(yourObjectName);
    }

    @Override
    public String upload(File file, String yourObjectName) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            return fastFileStorageClient.uploadFile(is, file.length(),
                    FilenameUtils.getExtension(yourObjectName), null).getFullPath();
        } catch (IOException e) {
            throw new FileIOException("fdfs upload local file error", e);
        }
    }

    @Override
    public String upload(InputStream is, String yourObjectName) {
        try {
            return fastFileStorageClient.uploadFile(is, is.available(),
                    getFileExtName(yourObjectName), null).getFullPath();
        } catch (IOException e) {
            throw new FileIOException("fdfs upload InputStream error", e);
        }
    }

    @Override
    public String upload(byte[] content, String yourObjectName) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            return fastFileStorageClient.uploadFile(is, content.length, getFileExtName(yourObjectName), null).getFullPath();
        } catch (IOException e) {
            throw new FileIOException("fdfs upload byte[] error", e);
        }
    }

    @Override
    public String upload(String url, String yourObjectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, yourObjectName);
        } catch (IOException e) {
            throw new FileIOException("fdfs upload url file error", e);
        }
    }

    @Override
    public String initUpload() {
        return uploadPartClient.initUpload();
    }

    @Override
    public void uploadPart(UploadPart part) {
        uploadPartClient.uploadPart(part);
    }

    @Override
    public void uploadParts(List<UploadPart> parts) {
        uploadPartClient.uploadParts(parts);
    }

    @Override
    public List<PartInfo> listParts(String uploadId) {
        return uploadPartClient.listParts(uploadId);
    }

    @Override
    public String complete(String uploadId, String ext) {
        StorePath storePath = null;
        List<UploadPart> parts = null;
        try {
            parts = uploadPartClient.listUploadParts(uploadId);
            parts.sort(Comparator.comparingInt(UploadPart::getPartNumber));
            for (int i = 0; i < parts.size(); i++) {
                UploadPart uploadPart = parts.get(i);
                if (i == 0) {
                    storePath = appendFileStorageClient.uploadAppenderFile(null, uploadPart.getInputStream(),
                            uploadPart.getPartSize(), ext);
                } else {
                    appendFileStorageClient.appendFile(storePath.getGroup(), storePath.getPath(),
                            uploadPart.getInputStream(), uploadPart.getPartSize());
                }
            }
        } finally {
            if (parts != null) {
                for (UploadPart part : parts) {
                    try {
                        part.getInputStream().close();
                    } catch (IOException e) {
                        logger.error("complete part InputStream close error", e);
                    }
                }
            }
        }
        if (storePath == null) {
            throw new FileIOException("fdfs complete part upload file error storePath is null");
        }
        return storePath.getFullPath();
    }

    @Override
    public void cancel(String uploadId, String yourObjectName, String groupName, String path) {
        //删除分片文件
        uploadPartClient.cancel(uploadId, yourObjectName);
        appendFileStorageClient.deleteFile(groupName, path);
    }

}
