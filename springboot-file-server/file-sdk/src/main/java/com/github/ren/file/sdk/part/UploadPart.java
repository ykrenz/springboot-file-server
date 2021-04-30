package com.github.ren.file.sdk.part;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @Description 分片信息
 * @Author ren
 * @Since 1.0
 */
public class UploadPart implements Serializable {
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
     * 分片文件流信息
     */
    private InputStream inputStream;

    /**
     * 分片md5值 如果添加了则会校验md5是否正确
     */
    private String md5Digest;

    public UploadPart(String uploadId, int partNumber, long partSize, InputStream inputStream) {
        this.uploadId = uploadId;
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.inputStream = inputStream;
    }

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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getMd5Digest() {
        return md5Digest;
    }

    public void setMd5Digest(String md5Digest) {
        this.md5Digest = md5Digest;
    }
}
