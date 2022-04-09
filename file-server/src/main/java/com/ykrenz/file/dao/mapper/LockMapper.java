package com.ykrenz.file.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykrenz.file.dao.mapper.entity.FileLockEntity;
import org.apache.ibatis.annotations.Insert;

/**
 * @author Mr Ren
 * @since 2021-05-24
 */
public interface LockMapper extends BaseMapper<FileLockEntity> {

    @Insert(
            "insert ignore into file_lock(lock_key,create_time,expire_time) values (#{lockKey},#{createTime},#{expireTime})"
    )
    int insertIgnore(FileLockEntity fileLockEntity);
}
