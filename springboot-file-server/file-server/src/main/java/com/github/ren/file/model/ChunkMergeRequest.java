package com.github.ren.file.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @date 2020/6/10 20:39
 */
@Data
public class ChunkMergeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
