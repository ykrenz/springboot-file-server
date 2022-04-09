package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "FastUploadRequest", description = "急速上传参数")
public class FastUploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "md5", value = "文件md5")
    private String md5;
    @ApiModelProperty(name = "fileName", value = "文件名称")
    private String fileName;
}
