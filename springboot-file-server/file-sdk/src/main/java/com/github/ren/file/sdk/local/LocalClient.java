package com.github.ren.file.sdk.local;

import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.UploadClient;
import com.github.ren.file.sdk.part.LocalPartClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Description 本地文件客户端
 * @Author ren
 * @Since 1.0
 */
public class LocalClient extends LocalPartClient implements UploadClient {

    private static final Logger logger = LoggerFactory.getLogger(LocalClient.class);

    public LocalClient(String partDir, String localStore) {
        super(partDir, localStore);
    }

    @Override
    public String upload(File file, String yourObjectName) {
        try {
            LocalFileOperation.copyFile(file, getOutFile(yourObjectName));
        } catch (IOException e) {
            throw new FileIOException("local upload file error", e);
        }
        return yourObjectName;
    }

    @Override
    public String upload(InputStream is, String yourObjectName) {
        try {
            LocalFileOperation.copyFile(is, getOutFile(yourObjectName));
        } catch (IOException e) {
            throw new FileIOException("local upload InputStream error", e);
        } finally {
            LocalFileOperation.close(is);
        }
        return yourObjectName;
    }

    @Override
    public String upload(byte[] content, String yourObjectName) {
        File outFile = this.getOutFile(yourObjectName);
        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            LocalFileOperation.copyFile(is, outFile);
        } catch (IOException e) {
            throw new FileIOException("local byte[] upload error", e);
        }
        return yourObjectName;
    }

    @Override
    public String upload(String url, String yourObjectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, yourObjectName);
        } catch (IOException e) {
            throw new FileIOException("local upload url file error", e);
        }
    }

}
