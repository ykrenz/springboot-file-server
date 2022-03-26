package com.ykrenz.fileserver.service.client;

public interface LockClient {
    /**
     * 尝试获取锁
     *
     * @return
     */
    boolean tryLock(String key);

    /**
     * 释放锁
     */
    void unlock(String key);
}
