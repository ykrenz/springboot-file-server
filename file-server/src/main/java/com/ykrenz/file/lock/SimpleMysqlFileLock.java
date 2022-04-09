package com.ykrenz.file.lock;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ykrenz.file.dao.mapper.entity.FileLockEntity;
import com.ykrenz.file.dao.mapper.LockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SimpleMysqlFileLock implements FileLock {

    @Resource
    private LockMapper lockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean tryLock(String key) {
        try {
            FileLockEntity lock = lockMapper.selectOne(Wrappers.<FileLockEntity>lambdaQuery().eq(FileLockEntity::getLockKey, key));
            if (lock != null) {
                // 防止宕机等意外
                if (isExpire(lock)) {
                    unlock(key);
                }
                return false;
            }
            lock = new FileLockEntity();
            lock.setLockKey(key);
            lock.setCreateTime(LocalDateTime.now());
            lock.setExpireTime(LocalDateTime.now().plusHours(1));
            return lockMapper.insertIgnore(lock) == 1;
        } catch (Exception e) {
            log.error("获取锁失败", e);
            return false;
        }
    }

    private boolean isExpire(FileLockEntity lock) {
        return lock.getExpireTime().isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlock(String key) {
        lockMapper.delete(Wrappers.<FileLockEntity>lambdaQuery().eq(FileLockEntity::getLockKey, key));
    }
}
