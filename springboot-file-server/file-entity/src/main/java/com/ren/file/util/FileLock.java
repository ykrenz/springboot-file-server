package com.ren.file.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileLock {

    private static Map<String, Lock> LOCKS = new HashMap<>();

    /**
     * 获取锁
     *
     * @param key
     */
    public static synchronized Lock getLock(String key) {
        if (LOCKS.containsKey(key)) {
            return LOCKS.get(key);
        } else {
            Lock one = new ReentrantLock();
            LOCKS.put(key, one);
            return one;
        }
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public static synchronized void removeLock(String key) {
        LOCKS.remove(key);
    }
}
