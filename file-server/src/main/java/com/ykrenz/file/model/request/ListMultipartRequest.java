package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "查询已经上传的分片", description = "查询已经上传的分片")
public class ListMultipartRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上传文件唯一标识")
    private String uploadId;
}