package com.github.ren.file.server.model.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分片信息
 * @Author ren
 * @Since 1.0
 */
@Data
public class PartResult implements Serializable {

    private static final long serialVersionUID = 1L;

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