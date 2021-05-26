package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 初始化分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("初始化分片上传参数")
public class InitPartRequest {

    @NotBlank(message = "filename不能为空")
    private String filename;

}
