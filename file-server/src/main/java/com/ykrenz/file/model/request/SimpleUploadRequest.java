package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 简单上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "SimpleUploadRequest", description = "简单文件")
public class SimpleUploadRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件
     */
    @ApiModelProperty(name = "file", value = "文件", required = true)
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

}
