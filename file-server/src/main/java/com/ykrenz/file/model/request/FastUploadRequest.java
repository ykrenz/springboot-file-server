package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "FastUploadRequest", description = "极速上传")
public class FastUploadRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "md5", value = "文件md5", required = true)
    @NotBlank(message = "不能为空")
    private String md5;

    @ApiModelProperty(name = "fileName", value = "文件名称", required = true)
    @NotBlank(message = "不能为空")
    private String fileName;
}
