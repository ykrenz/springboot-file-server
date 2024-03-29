package com.ykrenz.fileserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykrenz.fileserver.entity.FileLock;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 * 文件信息主表 Mapper 接口
 * </p>
 *
 * @author Mr Ren
 * @since 2021-05-24
 */
public interface LockMapper extends BaseMapper<FileLock> {

    @Insert(
            "insert ignore into file_lock(lock_key,create_time,expire_time) values (#{lockKey},#{createTime},#{expireTime})"
    )
    int insertIgnore(FileLock fileLock);
}
