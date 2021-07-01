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
 *
 * </p>
 *
 * @author Mr Ren
 * @since 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TUpload对象", description = "")
public class TUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("uploadId")
    private String uploadId;

    @TableField("objectName")
    private String objectName;

    @ApiModelProperty(value = "文件存储方式 1 本地 2 fastdfs 3 minio 4 alioss ...")
    @TableField("storage")
    private Integer storage;

    @ApiModelProperty(value = "过期时间(过期后不可使用 null为不过期需要手动清理)")
    @TableField("expireAt")
    private LocalDateTime expireAt;

    @ApiModelProperty(value = "文件大小")
    @TableField("fileSize")
    private Long fileSize;

    @ApiModelProperty(value = "分片大小")
    @TableField("partSize")
    private Long partSize;

    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0初始化 1完成 -1取消")
    private Integer status;

}
