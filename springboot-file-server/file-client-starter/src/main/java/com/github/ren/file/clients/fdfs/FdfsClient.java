package com.github.ren.file.clients.fdfs;

import com.github.ren.file.clients.FileIOException;
import com.github.ren.file.clients.UploadClient;
import com.github.ren.file.clients.UploadPart;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description fastdfs客户端
 * @Author ren
 * @Since 1.0
 */
public class FdfsClient extends FdfsAdapter implements UploadClient {

    public FdfsClient(GenerateStorageClient generateStorageClient,
                      FastFileStorageClient fastFileStorageClient,
                      AppendFileStorageClient appendFileStorageClient) {
        super(generateStorageClient, fastFileStorageClient, appendFileStorageClient);
    }

    public String getFileExtName(String yourObjectName){
        return FilenameUtils.getExtension(yourObjectName);
    }
    @Override
    public String uploadFile(File file, String yourObjectName) {
        try (InputStream is = FileUtils.openInputStream(file)) {
            return fastFileStorageClient.uploadFile(is, file.length(),
                    FilenameUtils.getExtension(yourObjectName), null).getFullPath();
        } catch (IOException e) {
            throw new FileIOException("fdfs upload local file error", e);
        }
    }

    @Override
    public String uploadFile(InputStream is, String yourObjectName) {
        try {
            return fastFileStorageClient.uploadFile(is, is.available(),
                    getFileExtName(yourObjectName), null).getFullPath();
        } catch (IOException e) {
            throw new FileIOException("fdfs upload InputStream error", e);
        }
    }

    @Override
    public String uploadFile(byte[] content, String yourObjectName) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            return fastFileStorageClient.uploadFile(is, content.length, getFileExtName(yourObjectName), null).getFullPath();
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    @Override
    public String uploadFile(String url, String yourObjectName) {
        return null;
    }

    @Override
    public String uploadPart(List<UploadPart> parts, String yourObjectName) {
        return null;
    }

    @Override
    public void close(InputStream is) {

    }
}
