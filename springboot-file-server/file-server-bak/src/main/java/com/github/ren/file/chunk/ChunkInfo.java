package com.github.ren.file.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Mr Ren
 * @Description: 分片信息
 * @date 2021/4/10 12:09
 */
@Data
@AllArgsConstructor
public class ChunkInfo implements Chunk {
    private String key;
    private int number;
    private long size;
}
