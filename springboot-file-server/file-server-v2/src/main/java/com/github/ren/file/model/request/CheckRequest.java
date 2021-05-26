package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 检测文件参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("检测文件参数")
public class CheckRequest {
    @NotBlank(message = "md5不能为空")
    private String md5;
}
