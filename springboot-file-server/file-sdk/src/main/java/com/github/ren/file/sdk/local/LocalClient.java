package com.github.ren.file.sdk.local;

import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.part.PartCancel;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.PartStore;
import com.github.ren.file.sdk.part.UploadPart;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description 本地文件客户端
 * @Author ren
 * @Since 1.0
 */
public class LocalClient implements FileClient {

    private static final Logger logger = LoggerFactory.getLogger(LocalClient.class);

    /**
     * 文件存储目录
     */
    private String localStore;

    private PartStore partStore;

    private PartCancel partCancel;

    public LocalClient(String localStore) {
        this.localStore = localStore;
    }

    public LocalClient(String localStore, PartStore partStore) {
        this.localStore = localStore;
        this.partStore = partStore;
    }

    public LocalClient(String localStore, PartStore partStore, PartCancel partCancel) {
        this.localStore = localStore;
        this.partStore = partStore;
        this.partCancel = partCancel;
    }

    public String getLocalStore() {
        File fileDir = new File(localStore);
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("localStore mkdirs error");
        }
        return localStore;
    }

    public void setLocalStore(String localStore) {
        this.localStore = localStore;
    }

    public PartStore getPartStore() {
        return partStore;
    }

    public void setPartStore(PartStore partStore) {
        this.partStore = partStore;
    }

    public PartCancel getPartCancel() {
        return partCancel;
    }

    public void setPartCancel(PartCancel partCancel) {
        this.partCancel = partCancel;
    }

    public File getOutFile(String yourObjectName) {
        String relativePath = Paths.get(getLocalStore(), yourObjectName).toString();
        File fileDir = new File(relativePath).getParentFile();
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("local mkdirs error");
        }
        return new File(relativePath);
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

    @Override
    public String merge(String uploadId, String yourObjectName) {
        List<UploadPart> parts = partStore.listUploadParts(uploadId);
        parts.sort(Comparator.comparingInt(UploadPart::getPartNumber));
        File outFile = this.getOutFile(yourObjectName);
        try (FileChannel outChannel = new FileOutputStream(outFile).getChannel()) {
            //同步nio 方式对分片进行合并, 有效的避免文件过大导致内存溢出
            for (UploadPart uploadPart : parts) {
                long chunkSize = 1L << 32;
                if (uploadPart.getPartSize() >= chunkSize) {
                    throw new RuntimeException("文件分片必须<4G");
                }
                try (FileChannel inChannel = ((FileInputStream) uploadPart.getInputStream()).getChannel()) {
                    int position = 0;
                    long size = inChannel.size();
                    while (0 < size) {
                        long count = inChannel.transferTo(position, size, outChannel);
                        if (count > 0) {
                            position += count;
                            size -= count;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new FileIOException("local complete file error", e);
        } finally {
            for (UploadPart part : parts) {
                LocalFileOperation.close(part.getInputStream());
            }
        }
        return yourObjectName;
    }

    @Override
    public void cancel(String uploadId, String yourObjectName) {
        partCancel.setCancel(uploadId);
        while (true) {
            if (partCancel.cancelSuccess(uploadId)) {
                partStore.del(uploadId);
                FileUtils.deleteQuietly(new File(getLocalStore(), yourObjectName));
                return;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String initUpload() {
        return partStore.initUpload();
    }

    @Override
    public void uploadPart(UploadPart part) {
        partStore.uploadPart(part);
    }

    @Override
    public List<PartInfo> listParts(String uploadId) {
        return partStore.listParts(uploadId);
    }
}
