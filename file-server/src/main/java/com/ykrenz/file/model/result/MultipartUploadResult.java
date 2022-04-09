package com.ykrenz.file.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 分片上传检测返回
 * @Author ren
 * @Since 1.0
 */
@ApiModel(value = "查询分片结果", description = "查询分片结果")
@Data
public class MultipartUploadResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("上传文件唯一标识")
    private String uploadId;
    @ApiModelProperty("文件名称")
    private String fileName;
    @ApiModelProperty("文件大小")
    private long fileSize;
    @ApiModelProperty("已经上传的分片数据 断点续传客户端可以跳过这些分片")
    private List<Integer> parts = new ArrayList<>(0);

}
