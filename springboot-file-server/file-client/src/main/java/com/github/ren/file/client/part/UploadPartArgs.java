package com.github.ren.file.client.part;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * @Description 分片信息
 * @Author ren
 * @Since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPartArgs {
    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * 上传唯一标识
     */
    private String objectName;

    /**
     * 分片索引
     */
    private int partNumber;

    /**
     * 文件总大小 fastdfs使用
     */
    private long fileSize;

    /**
     * 分片大小
     */
    private long partSize;

    /**
     * 分片文件流信息
     */
    private InputStream inputStream;

    //oss使用
    public UploadPartArgs(String uploadId, String objectName, int partNumber, long partSize, InputStream inputStream) {
        this.uploadId = uploadId;
        this.objectName = objectName;
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.inputStream = inputStream;
    }
}
