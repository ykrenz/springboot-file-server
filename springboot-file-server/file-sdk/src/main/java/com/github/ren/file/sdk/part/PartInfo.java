package com.github.ren.file.sdk.part;

import java.io.Serializable;

/**
 * @Description 分片信息
 * @Author ren
 * @Since 1.0
 */
public class PartInfo implements Serializable {
    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * 分片索引
     */
    private int partNumber;

    /**
     * 分片大小
     */
    private long partSize;

    /**
     * 分片eTag值
     */
    private String eTag;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public long getPartSize() {
        return partSize;
    }

    public void setPartSize(long partSize) {
        this.partSize = partSize;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }
}
