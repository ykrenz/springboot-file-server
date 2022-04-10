package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 初始化分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "InitUploadMultipartRequest", description = "初始化分片上传")
public class InitUploadMultipartRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "fileName", value = "文件名", required = true)
    @NotBlank(message = "不能为空")
    private String fileName;

    @ApiModelProperty(name = "fileSize", value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    @Min(value = 0, message = "文件大小不能为空")
    private Long fileSize;

    @ApiModelProperty(name = "partSize", value = "分片大小5~50MB", required = true)
    @NotNull(message = "分片大小不能为空")
    private Long partSize;

}
