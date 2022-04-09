package com.ykrenz.file.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "初始化分片上传结果", description = "初始化分片上传结果")
public class InitUploadMultipartResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上传文件唯一标识")
    private String uploadId;

    @ApiModelProperty(value = "校验码方式(md5,crc32,crc64) 客户端必须采用相同的校验完成上传 返回空则不进行校验")
    private String crc;

    @ApiModelProperty(value = "过期时间 过期后uploadId不可用 -1为永不过期")
    private long expireTime;
}
