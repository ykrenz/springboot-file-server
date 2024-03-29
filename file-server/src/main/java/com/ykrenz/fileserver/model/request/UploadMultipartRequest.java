package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "UploadMultipartRequest", description = "分片上传")
public class UploadMultipartRequest {

    @ApiModelProperty(name = "uploadId", value = "分片上传标识")
    @NotBlank(message = "分片上传标识不能为空")
    private String uploadId;

    @ApiModelProperty(name = "partNumber", value = "分片索引 从0开始")
    @NotNull(message = "分片索引不能为空")
    private Integer partNumber;

    @ApiModelProperty(name = "file", value = "分片文件")
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
}
