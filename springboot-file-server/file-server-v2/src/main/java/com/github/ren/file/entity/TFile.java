package com.github.ren.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value="TFile对象", description="文件信息主表")
public class TFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "文件md5值 客户端计算")
    private String md5;

    @ApiModelProperty(value = "文件服务端响应的eTag")
    @TableField("eTag")
    private String etag;

    @ApiModelProperty(value = "文件大小b")
    private Long filesize;

    @ApiModelProperty(value = "存储桶里的对象名称")
    @TableField("objectName")
    private String objectName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态 1 正常 -1删除")
    private Integer status;


}
