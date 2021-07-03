package com.github.ren.file.clients;

import java.io.InputStream;

/**
 * @author Mr Ren
 * @Description: 分片信息
 * @date 2021/4/11 11:49
 */
public class UploadPart {
    private int partNumber;
    private long partSize;
    private InputStream inputStream;

    public UploadPart(int partNumber, long partSize, InputStream inputStream) {
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.inputStream = inputStream;
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
}
