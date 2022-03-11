package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "SimpleUploadRequest", description = "上传简单文件")
public class SimpleUploadRequest {

    /**
     * 文件
     */
    @ApiModelProperty(name = "file", value = "文件")
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

    /**
     * md5验证
     */
    @ApiModelProperty(name = "md5", value = "文件md5值 fastdfs只保存不做验证")
    private String md5;

    /**
     * fastdfs crc32验证
     */
    @ApiModelProperty(name = "crc32", value = "文件crc32值 fastdfs保证数据完整性")
    private Long crc32;

    /**
     * 是否返回文件信息
     */
    @ApiModelProperty(name = "info", value = "是否返回文件信息")
    private boolean info;
}
