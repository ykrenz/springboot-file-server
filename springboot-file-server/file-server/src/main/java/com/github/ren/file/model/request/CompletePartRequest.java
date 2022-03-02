package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 合并分片参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("合并分片参数")
public class CompletePartRequest {
    @NotBlank(message = "不能为空")
    private String uploadId;
    /**
     * 文件md5
     */
    @ApiModelProperty(name = "fileMd5", value = "文件md5值", notes = "fastdfs只保存用于妙传不做验证")
    private String fileMd5;
    /**
     * fastdfs可使用 保证数据完整性
     */
    @ApiModelProperty(name = "fileCrc32", value = "文件crc32值", notes = "fastdfs使用 保证数据完整性")
    private Long fileCrc32;
}
