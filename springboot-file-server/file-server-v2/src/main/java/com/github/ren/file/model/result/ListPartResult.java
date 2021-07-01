package com.github.ren.file.model.result;

import com.github.ren.file.sdk.part.UploadMultipartResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 查询上传结果
 * @Author ren
 * @Since 1.0
 */
@Data
public class ListPartResult {
    @ApiModelProperty("uploadId")
    private String uploadId;
    @ApiModelProperty("已经上传的分片数据 断点续传客户端可以跳过这些分片")
    private List<UploadMultipartResponse> parts;
}
