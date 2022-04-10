package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "FastUploadRequest", description = "极速秒传")
public class FastUploadRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long sliceSize = 1024 * 8;

    @ApiModelProperty(name = "hash", value = "文件hash值", required = true)
    @NotBlank(message = "不能为空")
    private String hash;

    @ApiModelProperty(name = "sliceMd5", value = "文件分段md5值,取固定8kb,文件总大小必须大于8k", required = true)
    @NotBlank(message = "不能为空")
    private String sliceMd5;

    @ApiModelProperty(name = "fileName", value = "文件名称", required = true)
    @NotBlank(message = "不能为空")
    private String fileName;

    @ApiModelProperty(name = "fileSize", value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    @Min(value = sliceSize, message = "文件过小")
    private Long fileSize;
}
