package com.github.ren.file.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.github.ren.file.auth.AuthUser;
import com.github.ren.file.auth.UserContext;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr Ren
 * @Description: 实体基础类
 * @date 2021/4/6 17:59
 */
@Setter
@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 初始化用户信息
     */
    public BaseEntity() {
        AuthUser authUser = UserContext.getAuthUser();
        if (authUser != null) {
            this.creater = authUser.getUserId();
            this.updater = authUser.getUserId();
        }
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    @TableField("updater")
    private String updater;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField("creater")
    private String creater;

    /**
     * 乐观锁 版本控制
     */
    @ApiModelProperty(value = "乐观锁 版本控制")
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 逻辑删除配置 状态 0 禁用 1 正常 -1 删除
     */
    @ApiModelProperty(value = "状态 1 正常 -1 删除")
    @TableField("status")
    @TableLogic(delval = "-1", value = "1")
    private Integer status;
}
