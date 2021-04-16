package com.github.ren.file.lock;

/**
 * @author Mr Ren
 * @Description: 文件锁
 * @date 2021/4/6 10:29
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
