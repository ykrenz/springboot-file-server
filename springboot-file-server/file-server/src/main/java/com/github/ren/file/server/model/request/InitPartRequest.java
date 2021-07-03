package com.github.ren.file.server.model.request;

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

    @ApiModelProperty(name = "filename", value = "文件名")
    @NotBlank(message = "不能为空")
    private String filename;

    @ApiModelProperty(name = "filesize", value = "文件大小")
    @NotNull(message = "不能为空")
    private Long filesize;

    @ApiModelProperty(name = "partsize", value = "分片大小")
    @NotNull(message = "不能为空")
    private Long partsize;

//    @ApiModelProperty(name = "md5", value = "文件md5值")
//    private String md5;
}
