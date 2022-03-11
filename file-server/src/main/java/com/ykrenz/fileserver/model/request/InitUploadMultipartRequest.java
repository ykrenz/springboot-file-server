package com.ykrenz.fileserver.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 初始化分片上传参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel(value = "InitUploadMultipartRequest", description = "初始化分片上传")
public class InitUploadMultipartRequest {

    @ApiModelProperty(name = "md5", value = "文件md5", notes = "客户端秒传")
    private String fileMd5;

    @ApiModelProperty(name = "crc32", value = "文件crc32 fastdfs可结合md5秒传")
    private Long fileCrc32;

    @ApiModelProperty(name = "uploadId", value = "上传唯一标识 服务端返回的uploadId 可用于查询已经上传过的分片 断点续传")
    private String uploadId;

    @ApiModelProperty(name = "fileName", value = "文件名")
    @NotBlank(message = "不能为空")
    private String fileName;

    @ApiModelProperty(name = "fileSize", value = "文件大小")
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    @ApiModelProperty(name = "partSize", value = "分片大小")
    @NotNull(message = "分片大小不能为空")
    private Long partSize;
}
