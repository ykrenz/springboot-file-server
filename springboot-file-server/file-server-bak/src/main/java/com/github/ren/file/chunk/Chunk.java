package com.github.ren.file.chunk;

import java.io.Serializable;

/**
 * @author Mr Ren
 * @Description: 分片信息接口
 * @date 2021/4/10 12:08
 */
public interface Chunk extends Serializable {
    /**
     * 获取分片唯一标识 local 对应文件名称 redis对应文件key
     *
     * @return
     */
    String getKey();

    /**
     * 获取分片索引
     *
     * @return
     */
    int getNumber();

    /**
     * 获取分片大小
     *
     * @return
     */
    long getSize();

}
