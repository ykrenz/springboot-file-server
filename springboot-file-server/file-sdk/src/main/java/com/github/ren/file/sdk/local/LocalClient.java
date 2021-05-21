package com.github.ren.file.sdk.local;

import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.ex.FileIOException;
import com.github.ren.file.sdk.model.UploadGenericResult;
import com.github.ren.file.sdk.part.*;
import com.github.ren.file.sdk.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

    protected static final String PART_DIR = "/data/part";

    private PartStore partStore = new LocalPartStore(PART_DIR);

    public LocalClient(String localStore) {
        this.localStore = localStore;
    }

    public LocalClient(String localStore, PartStore partStore) {
        this.localStore = localStore;
        this.partStore = partStore;
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

    public String getLocalStore() {
        File fileDir = new File(localStore);
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("localStore mkdirs error");
        }
        return localStore;
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
    public UploadGenericResult upload(File file, String yourObjectName) {
        try {
            File outFile = getOutFile(yourObjectName);
            LocalFileOperation.copyFile(file, outFile);
            return new UploadGenericResult(yourObjectName, Util.eTag(outFile));
        } catch (IOException e) {
            throw new FileIOException("local upload file error", e);
        }
    }

    @Override
    public UploadGenericResult upload(InputStream is, String yourObjectName) {
        try {
            LocalFileOperation.copyFile(is, getOutFile(yourObjectName));
            return new UploadGenericResult(yourObjectName, Util.eTag(is));
        } catch (IOException e) {
            throw new FileIOException("local upload InputStream error", e);
        } finally {
            Util.close(is);
        }
    }

    @Override
    public UploadGenericResult upload(byte[] content, String yourObjectName) {
        File outFile = this.getOutFile(yourObjectName);
        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            LocalFileOperation.copyFile(is, outFile);
            return new UploadGenericResult(yourObjectName, Util.eTag(outFile));
        } catch (IOException e) {
            throw new FileIOException("local byte[] upload error", e);
        }
    }

    @Override
    public UploadGenericResult upload(String url, String yourObjectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, yourObjectName);
        } catch (IOException e) {
            throw new FileIOException("local upload url file error", e);
        }
    }

    @Override
    public InitMultipartResult initiateMultipartUpload(String yourObjectName) {
        String uploadId = UUID.randomUUID().toString().replace("-", "");
        return new InitMultipartResult(uploadId, yourObjectName);
    }

    @Override
    public PartInfo uploadPart(UploadPart part) {
        PartInfo partInfo = new PartInfo();
        partInfo.setPartSize(part.getPartSize());
        partInfo.setUploadId(part.getUploadId());
        partInfo.setPartNumber(part.getPartNumber());
        partInfo.setETag(partStore.uploadPart(part));
        return partInfo;
    }

    @Override
    public List<PartInfo> listParts(String uploadId, String yourObjectName) {
        return partStore.listParts(uploadId, yourObjectName);
    }

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName) {
        List<PartInfo> partInfos = listParts(uploadId, yourObjectName);
        partInfos.sort(Comparator.comparingInt(PartInfo::getPartNumber));
        File outFile = this.getOutFile(yourObjectName);
        try (FileChannel outChannel = new FileOutputStream(outFile).getChannel()) {
            //同步nio 方式对分片进行合并, 有效的避免文件过大导致内存溢出
            for (PartInfo partInfo : partInfos) {
                UploadPart uploadPart = partStore.getUploadPart(partInfo.getUploadId(), yourObjectName, partInfo.getPartNumber());
                try {
                    long chunkSize = 1L << 32;
                    if (uploadPart.getPartSize() >= chunkSize) {
                        throw new IllegalArgumentException("文件分片必须<4G");
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
                } finally {
                    Util.close(uploadPart.getInputStream());
                }

            }
        } catch (IOException e) {
            throw new FileIOException("local complete file error", e);
        }
        CompleteMultipart completeMultipart = new CompleteMultipart();
        completeMultipart.setObjectName(yourObjectName);
        completeMultipart.setETag(Util.eTag(outFile));
        return completeMultipart;
    }

    @Override
    public void abortMultipartUpload(String uploadId, String yourObjectName) {
        //TODO abortMultipartUpload
    }

}
