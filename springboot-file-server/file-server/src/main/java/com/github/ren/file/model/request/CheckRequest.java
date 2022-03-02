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
    @ApiModelProperty(name = "md5", value = "文件md5", notes = "秒传")
    private String fileMd5;

    @ApiModelProperty(name = "crc32", value = "文件crc32", notes = "fastdfs结合md5秒传")
    private Long fileCrc32;

    @ApiModelProperty(name = "uploadId", value = "上传唯一标识", notes = "用于断点续传 返回已经上传的分片序号")
    private String uploadId;
}
