package com.ykrenz.fileserver.model.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 查询上传分片结果
 * @Author ren
 * @Since 1.0
 */
@Data
public class ListPartResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("uploadId")
    private String uploadId;

    @ApiModelProperty("已经上传的分片数据 断点续传客户端可以跳过这些分片")
    private List<PartResult> parts;

}
