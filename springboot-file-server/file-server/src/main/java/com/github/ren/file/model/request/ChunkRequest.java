package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Mr Ren
 * @Description: 分块信息
 * @date 2020/6/10 20:39
 */
@Data
@ApiModel("分块信息")
public class ChunkRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件标识md5
     */
    @ApiModelProperty("文件标识md5")
    @NotNull(message = "文件md5值不能为空")
    private String md5;

    /**
     * 当前文件块索引，从1开始
     */
    @ApiModelProperty("当前文件块索引，从1开始")
    @NotNull(message = "当前文件块索引不能为空")
    private Integer chunkNumber;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    @NotNull(message = "filename文件名称不能为空")
    private String filename;

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
     * 总块数
     */
    @ApiModelProperty("总块数")
    private Integer totalChunks;
}