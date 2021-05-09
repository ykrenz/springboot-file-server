package com.github.ren.file.sdk.part;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 内存取消状态 适用于单体环境
 * @Author ren
 * @Since 1.0
 */
public class MemoryCancel implements PartCancel {

    private static final Map<String, Object> CANCEL_MAP = new HashMap<>();

    @Override
    public void setCancel(String uploadId) {
        CANCEL_MAP.put(uploadId, 1);
    }

    @Override
    public boolean needCancel(String uploadId) {
        return CANCEL_MAP.get(uploadId) != null;
    }

    @Override
    public void cancelComplete(String uploadId) {
        CANCEL_MAP.remove(uploadId);
    }
}
