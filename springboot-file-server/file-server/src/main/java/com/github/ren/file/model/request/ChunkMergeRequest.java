package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Mr Ren
 * @Description: 分片合并请求
 * @date 2020/6/10 20:39
 */
@Data
@ApiModel("分片合并请求")
public class ChunkMergeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名称")
    @NotNull(message = "文件名称不能为空")
    private String filename;

    @NotNull(message = "文件md5值不能为空")
    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @NotNull(message = "文件大小不能为空")
    @ApiModelProperty(value = "文件大小b")
    private Long size;
}
