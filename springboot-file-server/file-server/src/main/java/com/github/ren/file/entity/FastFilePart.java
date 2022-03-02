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
@ApiModel(value = "FastFilePart对象", description = "FastDFS分片文件")
public class FastFilePart extends BaseEntity {

    @ApiModelProperty(value = "分片索引")
    @TableField("uploadId")
    private String uploadId;

    @ApiModelProperty(value = "分片索引")
    @TableField("partNumber")
    private Integer partNumber;

    @ApiModelProperty(value = "文件大小")
    @TableField("fileSize")
    private Long fileSize;

    @ApiModelProperty(value = "分片大小")
    @TableField("partSize")
    private Long partSize;

    @ApiModelProperty(value = "groupName")
    @TableField("groupName")
    private String groupName;

    @ApiModelProperty(value = "path")
    private String path;

}
