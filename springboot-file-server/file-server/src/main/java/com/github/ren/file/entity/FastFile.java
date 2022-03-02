package com.github.ren.file.entity;

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
@ApiModel(value = "FastFile对象", description = "FastDFS文件")
public class FastFile extends BaseEntity {

    @ApiModelProperty(value = "文件md5值 客户端计算")
    private String md5;

    @ApiModelProperty(value = "文件crc32值")
    private Long crc32;

    @ApiModelProperty(value = "文件大小b")
    @TableField("fileSize")
    private Long fileSize;

    @ApiModelProperty(value = "groupName")
    @TableField("groupName")
    private String groupName;

    @ApiModelProperty(value = "path")
    private String path;
}
