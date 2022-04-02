package com.ykrenz.fileserver.service.client;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ykrenz.fileserver.entity.FileLock;
import com.ykrenz.fileserver.mapper.LockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SimpleMysqlLock implements LockClient {

    @Resource
    private LockMapper lockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean tryLock(String key) {
        try {
            FileLock lock = lockMapper.selectOne(Wrappers.<FileLock>lambdaQuery().eq(FileLock::getLockKey, key));
            if (lock != null) {
                // 防止宕机等意外
                if (isExpire(lock)) {
                    unlock(key);
                }
                return false;
            }
            lock = new FileLock();
            lock.setLockKey(key);
            lock.setCreateTime(LocalDateTime.now());
            lock.setExpireTime(LocalDateTime.now().plusHours(1));
            return lockMapper.insertIgnore(lock) == 1;
        } catch (Exception e) {
            log.error("获取锁失败", e);
            return false;
        }
    }

    private boolean isExpire(FileLock lock) {
        return lock.getExpireTime().isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlock(String key) {
        lockMapper.delete(Wrappers.<FileLock>lambdaQuery().eq(FileLock::getLockKey, key));
    }
}
