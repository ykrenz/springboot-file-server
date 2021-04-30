package com.github.ren.file.sdk.part;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 内存取消状态 适用于单体环境
 * @Author ren
 * @Since 1.0
 */
public class MemoryCancel implements PartCancel {

    private static final Map<String, Object> cancelMap = new ConcurrentHashMap<>();

    @Override
    public void setCancel(String uploadId) {
        cancelMap.put(uploadId, 1);
    }

    @Override
    public boolean needCancel(String uploadId) {
        return Integer.valueOf(1).equals(cancelMap.get(uploadId));
    }


    @Override
    public void cancelComplete(String uploadId) {
        cancelMap.remove(uploadId);
    }

    @Override
    public boolean cancelSuccess(String uploadId) {
        return cancelMap.get(uploadId) == null;
    }
}
