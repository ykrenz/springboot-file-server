package com.ykrenz.fileserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件信息主表
 * </p>
 *
 * @author Mr Ren
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CloudFile对象", description = "云存储文件")
public class CloudFile extends BaseEntity {

    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @ApiModelProperty(value = "文件服务端响应的eTag")
    @TableField("eTag")
    private String eTag;

    @ApiModelProperty(value = "文件大小b")
    private Long fileSize;

    @ApiModelProperty(value = "存储桶名称")
    @TableField("bucketName")
    private String bucketName;

    @ApiModelProperty(value = "存储桶里的对象名称")
    @TableField("objectName")
    private String objectName;

}
