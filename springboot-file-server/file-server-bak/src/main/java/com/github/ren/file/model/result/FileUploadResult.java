package com.github.ren.file.model.result;

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
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "文件大小b")
    private Long filesize;

    @ApiModelProperty(value = "文件路径")
    private String filepath;

    @ApiModelProperty(value = "访问地址")
    private String accessPath;

    @ApiModelProperty(value = "下载地址")
    private String downloadPath;
}
