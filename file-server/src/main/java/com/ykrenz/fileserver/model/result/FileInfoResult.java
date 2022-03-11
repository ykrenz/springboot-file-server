package com.ykrenz.fileserver.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel(value = "FileInfoResult", description = "文件信息")
public class FileInfoResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @ApiModelProperty(value = "文件crc32值")
    private Long crc32;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "bucketName", notes = "存储桶 fastdfs对应group")
    private String bucketName;

    @ApiModelProperty(value = "objectName", notes = "文件路径 fastdfs对应path")
    private String objectName;

    @ApiModelProperty(value = "webPath", notes = "文件预览路径")
    private String webPath;

    @ApiModelProperty(value = "downloadPath", notes = "文件下载路径")
    private String downloadPath;
}
