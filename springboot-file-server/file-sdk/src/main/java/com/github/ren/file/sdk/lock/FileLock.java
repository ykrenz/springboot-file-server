package com.github.ren.file.sdk.lock;

/**
 * @Description 文件锁
 * @Author ren
 * @Since 1.0
 */
public interface FileLock {
    /**
     * 加锁
     *
     * @param key
     */
    void lock(String key);

    /**
     * 释放锁
     *
     * @param key
     */
    void unlock(String key);
}
