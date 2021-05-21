package com.github.ren.file.sdk.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 本地文件锁
 * @Author ren
 * @Since 1.0
 */
public class LocalLock implements FileLock {

    private static final Logger logger = LoggerFactory.getLogger(LocalLock.class);

    /**
     * 默认锁数量
     */
    private static final int DEFAULT_LOCK_COUNT = 64;

    private final Map<Integer, ReentrantLock> lockMap = new HashMap<>();

    private int count;

    private boolean isFair;

    public LocalLock() {
        this(DEFAULT_LOCK_COUNT, false);
    }

    public LocalLock(boolean isFair) {
        this(DEFAULT_LOCK_COUNT, isFair);
    }

    public LocalLock(int count, boolean isFair) {
        this.count = count;
        this.isFair = isFair;
        this.init();
    }

    private void init() {
        if (count <= 0) {
            count = DEFAULT_LOCK_COUNT;
        }
        // 初始化指定数量的锁
        for (int i = 0; i < count; i++) {
            this.lockMap.put(i, new ReentrantLock(isFair));
        }
    }

    public Lock getLock(String key) {
        return this.lockMap.get((key.hashCode() >>> 1) % count);
    }

    @Override
    public void lock(String key) {
        this.getLock(key).lock();
    }

    @Override
    public void unlock(String key) {
        Lock lock = this.getLock(key);
        lock.unlock();
    }
}
