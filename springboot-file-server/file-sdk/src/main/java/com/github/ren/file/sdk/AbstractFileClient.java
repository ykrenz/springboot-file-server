package com.github.ren.file.sdk;

import com.github.ren.file.sdk.lock.FileLock;
import com.github.ren.file.sdk.lock.SegmentLock;
import com.github.ren.file.sdk.part.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Description 文件客户端抽象类
 * @Author ren
 * @Since 1.0
 */
public abstract class AbstractFileClient implements FileClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFileClient.class);

    protected static final String PART_DIR = "/data/part";

    protected PartStore partStore = new LocalPartStore(PART_DIR);

    protected PartCancel partCancel = new MemoryCancel();

    protected FileLock fileLock = new SegmentLock();

    public AbstractFileClient() {
    }

    public AbstractFileClient(PartStore partStore) {
        this.partStore = partStore;
    }

    public AbstractFileClient(PartStore partStore, PartCancel partCancel, FileLock fileLock) {
        this.partStore = partStore;
        this.partCancel = partCancel;
        this.fileLock = fileLock;
    }

    public FileLock getFileLock() {
        return fileLock;
    }

    public void setFileLock(FileLock fileLock) {
        this.fileLock = fileLock;
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

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName, String md5) {
        return null;
    }

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName) {
        try {
            fileLock.lock(uploadId);
            CompleteMultipart merge = merge(uploadId, yourObjectName);
            partStore.clear(uploadId, yourObjectName);
            return merge;
        } finally {
            fileLock.unlock(uploadId);
        }
    }

    @Override
    public PartInfo uploadPart(UploadPart part) {
        String uploadId = part.getUploadId();
        int partNumber = part.getPartNumber();
        String key = uploadId + partNumber;
        try {
            fileLock.lock(key);
            PartInfo partInfo = new PartInfo();
            partInfo.setPartSize(part.getPartSize());
            partInfo.setUploadId(part.getUploadId());
            partInfo.setPartNumber(part.getPartNumber());
            partInfo.setETag(uploadPartFile(part));
            return partInfo;
        } finally {
            fileLock.unlock(key);
        }
    }

    /**
     * 上传分片文件
     *
     * @param part 分片信息
     * @return md5值
     */
    protected abstract String uploadPartFile(UploadPart part);

    /**
     * 合并文件
     *
     * @param uploadId
     * @param yourObjectName
     * @return
     */
    protected abstract CompleteMultipart merge(String uploadId, String yourObjectName);

    @Override
    public void abortMultipartUpload(String uploadId, String yourObjectName) {
        partCancel.setCancel(uploadId);
        logger.info("abortMultipartUpload uploadId={}", uploadId);
        try {
            fileLock.lock(uploadId);
            while (true) {
                if (partStore.clear(uploadId, yourObjectName)) {
                    partCancel.cancelComplete(uploadId);
                    return;
                }
                try {
                    TimeUnit.MICROSECONDS.sleep(10);
                } catch (InterruptedException e) {
                    logger.error("abortMultipartUpload Thread InterruptedException uploadId={}", uploadId);
                }
            }
        } finally {
            fileLock.unlock(uploadId);
        }
    }
}
