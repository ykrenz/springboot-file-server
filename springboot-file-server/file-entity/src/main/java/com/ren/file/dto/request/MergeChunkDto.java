package com.ren.file.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date 2020/6/10 20:39
 */
@Data
public class MergeChunkDto {
    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "文件md5值")
    private String identifier;

    @ApiModelProperty(value = "文件大小b")
    private Long filesize;

    @ApiModelProperty(value = "文件后缀名")
    private String extension;

    @ApiModelProperty(value = "文件类型")
    private String fileType;
}
