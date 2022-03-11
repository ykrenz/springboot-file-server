package com.ykrenz.fileserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "FileInfo", description = "文件信息")
public class FileInfo extends BaseEntity {

    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @ApiModelProperty(value = "文件crc32值")
    private Long crc32;

    @ApiModelProperty(value = "文件名称")
    @TableField("fileName")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    @TableField("fileSize")
    private Long fileSize;

    @ApiModelProperty(value = "bucketName", notes = "存储桶 fastdfs对应group")
    @TableField("bucketName")
    private String bucketName;

    @ApiModelProperty(value = "objectName", notes = "文件路径 fastdfs对应path")
    @TableField("objectName")
    private String objectName;

}
