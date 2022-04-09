package com.ykrenz.file.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "文件信息", description = "文件信息")
public class FileResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "校验码")
    private String crc;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "存储桶")
    private String bucketName;

    @ApiModelProperty(value = "文件路径")
    private String objectName;

    @ApiModelProperty(value = "url地址")
    private String url;
}
