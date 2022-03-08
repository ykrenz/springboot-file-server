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
@ApiModel(value = "FilePartInfo对象", description = "分片文件")
public class FilePartInfo extends BaseEntity {

    @ApiModelProperty(value = "分片上传唯一标识")
    @TableField("uploadId")
    private String uploadId;

    @ApiModelProperty(value = "文件名称")
    @TableField("fileName")
    private String fileName;

    @ApiModelProperty(value = "分片索引")
    @TableField("partNumber")
    private Integer partNumber;

    @ApiModelProperty(value = "文件大小")
    @TableField("fileSize")
    private Long fileSize;

    @ApiModelProperty(value = "分片大小")
    @TableField("partSize")
    private Long partSize;

    @ApiModelProperty(value = "bucketName", notes = "存储桶 fastdfs对应group")
    @TableField("bucketName")
    private String bucketName;

    @ApiModelProperty(value = "objectName", notes = "文件路径 fastdfs对应path")
    @TableField("objectName")
    private String objectName;

}
