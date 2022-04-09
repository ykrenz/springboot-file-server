package com.ykrenz.file.dao.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected BaseEntity() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        this.createTime = now;
        this.updateTime = now;
        this.status = 1;
    }

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 状态 1 正常 -1删除 0 不可用
     */
    private Integer status;
}
