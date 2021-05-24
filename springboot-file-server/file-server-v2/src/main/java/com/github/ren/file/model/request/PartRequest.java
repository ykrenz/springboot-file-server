package com.github.ren.file.model.request;

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
public class PartRequest {
    @NotBlank(message = "uploadId不能为空")
    private String uploadId;
    @NotNull(message = "partNumber不能为空")
    private Integer partNumber;
    @NotNull(message = "partNumber不能为空")
    private MultipartFile file;
}
