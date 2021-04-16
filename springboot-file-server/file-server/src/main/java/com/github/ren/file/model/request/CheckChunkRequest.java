package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Mr Ren
 * @Description: 分块检测
 * @date 2021/4/1 14:25
 */
@Data
@ApiModel("分块检测")
public class CheckChunkRequest {
    /**
     * 文件标识
     */
    @NotNull(message = "文件标识不能为空")
    @ApiModelProperty("文件标识")
    private String md5;

    /**
     * 总块数
     */
    @NotNull(message = "文件名不能为空")
    @ApiModelProperty("文件名")
    private String filename;
}
