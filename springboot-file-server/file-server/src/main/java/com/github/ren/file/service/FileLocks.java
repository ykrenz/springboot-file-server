package com.github.ren.file.service;

/**
 * @author Mr Ren
 * @Description: 文件锁
 * @date 2021/4/6 10:29
 */
public interface FileLocks<T> {
    /**
     * 加锁
     *
     * @param key
     */
    void lock(T key);

    /**
     * 释放锁
     *
     * @param key
     */
    void unlock(T key);
}
