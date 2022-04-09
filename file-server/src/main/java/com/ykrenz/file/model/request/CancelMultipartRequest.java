package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 取消分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "CancelPartRequest", description = "取消分片上传")
public class CancelMultipartRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上传唯一标识
     */
    @ApiModelProperty(name = "uploadId", value = "上传唯一标识", required = true)
    @NotBlank(message = "不能为空")
    private String uploadId;
}
