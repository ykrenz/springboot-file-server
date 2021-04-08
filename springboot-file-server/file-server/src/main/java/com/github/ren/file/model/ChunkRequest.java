package com.github.ren.file.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("分块信息")
public class ChunkRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前文件块索引，从1开始
     */
    @ApiModelProperty("当前文件块索引，从1开始")
    private Long chunkNumber;
    /**
     * 分块大小
     */
    @ApiModelProperty("分块大小")
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    @ApiModelProperty("当前分块大小")
    private Long currentChunkSize;
    /**
     * 总大小
     */
    @ApiModelProperty("总大小")
    private Long totalSize;
    /**
     * 文件标识md5
     */
    @ApiModelProperty("文件标识md5")
    private String identifier;
    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String filename;
    /**
     * 总块数
     */
    @ApiModelProperty("总块数")
    private Integer totalChunks;
    /**
     * 文件类型
     */
    private String type;

    private String extension;

    private String fileType;
}