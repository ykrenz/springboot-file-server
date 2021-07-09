package com.github.ren.file.server.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分片上传响应信息
 * @Author ren
 * @Since 1.0
 */
@Data
public class UploadMultipartResponse implements Serializable {
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

}
