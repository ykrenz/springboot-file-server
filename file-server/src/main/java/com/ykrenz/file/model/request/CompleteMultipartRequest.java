package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 完成分片上传
 */
@Data
@ApiModel(value = "CompletePartRequest", description = "完成分片上传")
public class CompleteMultipartRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上传唯一标识
     */
    @ApiModelProperty(name = "uploadId", value = "上传唯一标识", required = true)
    @NotBlank(message = "不能为空")
    private String uploadId;

    /**
     * 校验码
     */
    @ApiModelProperty(name = "hash", value = "校验码,保证与checkMode类型一致")
    @NotBlank(message = "不能为空")
    private String hash;

}
