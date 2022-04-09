package com.ykrenz.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "UploadMultipartRequest", description = "分片上传")
public class UploadMultipartRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "uploadId", value = "上传唯一标识", required = true)
    @NotBlank(message = "不能为空")
    private String uploadId;

    @ApiModelProperty(name = "partNumber", value = "分片索引 从0开始", required = true)
    @NotNull(message = "不能为空")
    @Min(value = 1, message = "必须大于0")
    private Integer partNumber;

    @ApiModelProperty(name = "file", value = "分片文件", required = true)
    @NotNull(message = "不能为空")
    private MultipartFile file;
}
