package com.ykrenz.file.dao.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Data
@TableName("file_lock")
public class FileLockEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 锁key
     */
    @TableId
    private String lockKey;
    /**
     * 生成时间
     */
    private LocalDateTime createTime;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
