package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 取消分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "CancelPartRequest", description = "取消分片上传")
public class CancelPartRequest {

    /**
     * 上传唯一标识
     */
    @ApiModelProperty(name = "uploadId", value = "上传唯一标识")
    @NotBlank(message = "不能为空")
    private String uploadId;
}
