package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "FileInfoRequest", description = "文件信息")
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoRequest {

    @ApiModelProperty(name = "bucketName", value = "文件bucketName")
    @NotBlank(message = "存储桶不能为空")
    private String bucketName;

    @ApiModelProperty(name = "objectName", value = "文件objectName")
    @NotBlank(message = "文件路径不能为空")
    private String objectName;
}
