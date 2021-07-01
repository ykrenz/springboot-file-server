package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 检测文件参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("检测文件参数")
public class CheckRequest {
    @ApiModelProperty(name = "md5", value = "文件md5 用于秒传检测")
    private String md5;
    @ApiModelProperty(name = "uploadId", value = "分片唯一标识 用于断点续传")
    private String uploadId;
}
