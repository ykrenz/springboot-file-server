package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModelProperty;
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

    /**
     * md5验证
     */
    @ApiModelProperty(name = "md5", value = "文件md5值", notes = "fastdfs只保存用于妙传不做验证")
    private String md5;

    /**
     * fastdfs crc32验证
     */
    @ApiModelProperty(name = "crc32", value = "文件crc32值", notes = "fastdfs使用 保证数据完整性")
    private Long crc32;
}
