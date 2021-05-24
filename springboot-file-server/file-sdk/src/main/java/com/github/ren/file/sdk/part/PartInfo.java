package com.github.ren.file.sdk.part;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分片信息
 * @Author ren
 * @Since 1.0
 */
@Data
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

}
