package com.ykrenz.fileserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected BaseEntity() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = 1;
    }

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态 1 正常 -1删除 0 终止")
    private Integer status;
}
