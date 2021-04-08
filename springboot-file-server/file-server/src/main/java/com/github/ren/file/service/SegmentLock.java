package com.github.ren.file.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mr Ren
 * @Description: 分段锁
 * @date 2021/4/1 21:20
 */
public class SegmentLock<T> implements FileLocks<T> {

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

    public Lock getLock(T key) {
        return this.lockMap.get((key.hashCode() >>> 1) % count);
    }

    @Override
    public void lock(T key) {
        Lock lock = this.getLock(key);
        lock.lock();
    }

    @Override
    public void unlock(T key) {
        Lock lock = this.getLock(key);
        lock.unlock();
    }
}
