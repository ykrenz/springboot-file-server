package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("文件信息参数")
public class FileInfoRequest {

    @ApiModelProperty(name = "bucketName", value = "文件bucketName")
    @NotBlank(message = "不能为空")
    private String bucketName;

    @ApiModelProperty(name = "objectName", value = "文件objectName")
    @NotBlank(message = "不能为空")
    private String objectName;
}
