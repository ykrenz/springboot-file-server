package com.ykrenz.file.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "查询已经上传的分片", description = "查询已经上传的分片")
public class ListMultipartResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上传文件唯一标识")
    private String uploadId;

    @ApiModelProperty("文件大小")
    private long fileSize;

    @ApiModelProperty("分片大小")
    private long partSize;

    @ApiModelProperty("分片记录")
    private List<Integer> parts;
}
