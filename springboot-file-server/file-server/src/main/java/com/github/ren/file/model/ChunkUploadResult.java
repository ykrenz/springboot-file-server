package com.github.ren.file.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr Ren
 * @Description: 分片上传结果
 * @date 2021/4/8 13:14
 */
@Data
public class ChunkUploadResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上传状态 上传成功 分片已存在 3")
    private String status;

    @ApiModelProperty("上传成功文件信息")
    private FileUploadResult file;

}
