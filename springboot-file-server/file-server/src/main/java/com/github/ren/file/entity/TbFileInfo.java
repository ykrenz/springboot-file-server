package com.github.ren.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件信息从表
 * </p>
 *
 * @author Mr Ren
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TbFileInfo对象", description="文件信息从表")
public class TbFileInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "外键 关联tb_file表中主键")
    private String fid;

    @ApiModelProperty(value = "文件或文件夹名称")
    private String filename;

    @ApiModelProperty(value = "所属文件夹id")
    private String folder;


}
