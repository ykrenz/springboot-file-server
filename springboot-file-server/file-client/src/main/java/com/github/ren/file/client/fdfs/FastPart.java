package com.github.ren.file.client.fdfs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description fastdfs 分片信息
 * @Author ren
 * @Since 1.0
 */
@Data
public class FastPart implements Serializable {
    private int partNumber;
    private long partSize;
}
