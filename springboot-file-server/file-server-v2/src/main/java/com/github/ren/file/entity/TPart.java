package com.github.ren.file.entity;

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
 * 文件分片信息表
 * </p>
 *
 * @author Mr Ren
 * @since 2021-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TPart对象", description="文件分片信息表")
public class TPart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "分片上传返回的uploadId")
    @TableField("uploadId")
    private String uploadid;

    @ApiModelProperty(value = "分片文件所属的objectName")
    @TableField("objectName")
    private String objectname;

    @ApiModelProperty(value = "分片索引")
    @TableField("partNumber")
    private Integer partnumber;

    @ApiModelProperty(value = "分片大小")
    @TableField("partSize")
    private Long partsize;

    @ApiModelProperty(value = "分片上传完成后响应的eTag值")
    @TableField("eTag")
    private String etag;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态 0 就绪 1正常 -1删除")
    private Integer status;


}
