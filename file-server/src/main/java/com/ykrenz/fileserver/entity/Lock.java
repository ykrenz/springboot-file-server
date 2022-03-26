package com.ykrenz.fileserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Data
@ApiModel(value = "Lock", description = "锁")
public class Lock implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "锁key")
    private String lockKey;
    @ApiModelProperty(value = "生成时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;
}
