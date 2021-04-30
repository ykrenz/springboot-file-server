package com.github.ren.file.clients.fdfs;

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

import java.io.InputStream;
import java.util.Set;

/**
 * @Description fastdfs文件客戶端适配器
 * @Author ren
 * @Since 1.0
 */

public class FdfsAdapter implements FastFileStorageClient, AppendFileStorageClient {

    protected final FastFileStorageClient fastFileStorageClient;

    protected final AppendFileStorageClient appendFileStorageClient;

    public FdfsAdapter(DefaultFastFileStorageClient fastFileStorageClient,
                       DefaultAppendFileStorageClient appendFileStorageClient) {
        this.fastFileStorageClient = fastFileStorageClient;
        this.appendFileStorageClient = appendFileStorageClient;
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
}
