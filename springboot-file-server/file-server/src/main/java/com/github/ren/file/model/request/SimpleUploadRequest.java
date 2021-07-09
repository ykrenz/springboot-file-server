package com.github.ren.file.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @Description 简单上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
public class SimpleUploadRequest {
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
}
