package com.ykrenz.file.upload.storage.model;

import lombok.Data;

@Data
public class DownLoadRequest {
    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * 文件路径
     */
    private String objectName;

    /**
     * 文件起始和结束
     */
    private long[] range;

    public void setRange(long start, long end) {
        range = new long[] {start, end};
    }

    public long[] getRange() {
        return range == null ? null : range.clone();
    }
}
