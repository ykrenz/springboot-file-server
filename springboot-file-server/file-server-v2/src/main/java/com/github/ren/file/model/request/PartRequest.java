package com.github.ren.file.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/5/14 12:00
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
