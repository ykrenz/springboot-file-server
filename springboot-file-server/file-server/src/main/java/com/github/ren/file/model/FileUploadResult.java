package com.github.ren.file.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author Mr Ren
 * @Description: 文件上传结果
 * @date 2021/4/1 14:39
 */
@Data
@Builder
public class FileUploadResult {
    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @ApiModelProperty(value = "文件大小b")
    private Long filesize;

    @ApiModelProperty(value = "访问路径")
    private String path;
}
