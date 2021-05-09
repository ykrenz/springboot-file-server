package com.github.ren.file.sdk.part;

import java.io.Serializable;

/**
 * @Description 分片状态
 * @Author ren
 * @Since 1.0
 */
public interface PartCancel extends Serializable {

    /**
     * 设置取消状态
     *
     * @param uploadId
     */
    void setCancel(String uploadId);

    /**
     * 是否需要取消
     *
     * @param uploadId
     */
    boolean needCancel(String uploadId);

    /**
     * 取消完成
     *
     * @param uploadId
     */
    void cancelComplete(String uploadId);

}
