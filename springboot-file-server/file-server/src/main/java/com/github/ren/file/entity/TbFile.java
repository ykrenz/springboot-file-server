package com.github.ren.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TbFile对象", description="文件信息主表")
public class TbFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "文件md5值")
    private String md5;

    @ApiModelProperty(value = "文件大小b")
    private Long filesize;

    @ApiModelProperty(value = "文件路径 oss存储objectname fast存储fullpath")
    private String path;


}
