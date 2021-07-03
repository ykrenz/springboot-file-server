package com.github.ren.file.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Mr Ren
 * @Description: 上下文用户信息
 * @date 2021/2/22 9:26
 */
@Data
@ApiModel("登录授权用户信息")
public class AuthUser {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户ID")
    private String userId;

}