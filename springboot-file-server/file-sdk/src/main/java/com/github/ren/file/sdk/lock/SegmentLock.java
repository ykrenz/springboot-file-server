package com.github.ren.file.sdk.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 分段锁
 * @Author ren
 * @Since 1.0
 */
@Slf4j
public class SegmentLock implements FileLock {

    /**
     * 默认锁数量
     */
    private static final int DEFAULT_LOCK_COUNT = 16;

    private final Map<Integer, ReentrantLock> lockMap = new HashMap<>();

    private int count;

    private boolean isFair;

    public SegmentLock() {
        this(DEFAULT_LOCK_COUNT, false);
    }

    public SegmentLock(boolean isFair) {
        this(DEFAULT_LOCK_COUNT, isFair);
    }

    public SegmentLock(int count, boolean isFair) {
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
