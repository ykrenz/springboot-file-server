package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 合并分片参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("合并分片参数")
public class CompletePartRequest {

    @NotBlank(message = "不能为空")
    private String uploadId;
    private String md5;
}
