package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 初始化分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("初始化分片上传参数")
public class InitPartRequest {

    @ApiModelProperty(name = "标识id", value = "文件唯一标识", notes = "每个上传文件不能重复 例如uuid md5等")
    @NotBlank(message = "不能为空")
    private String uploadId;

    @ApiModelProperty(name = "fileName", value = "文件名")
    @NotBlank(message = "不能为空")
    private String fileName;

    @ApiModelProperty(name = "fileSize", value = "文件大小")
    @NotNull(message = "不能为空")
    private Long fileSize;

    @ApiModelProperty(name = "partSize", value = "分片大小")
    @NotNull(message = "不能为空")
    private Long partSize;
}
