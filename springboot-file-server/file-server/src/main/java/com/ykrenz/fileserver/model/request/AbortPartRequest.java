package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 取消分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("取消分片上传参数")
public class AbortPartRequest {
    @NotBlank(message = "不能为空")
    private String uploadId;
}
