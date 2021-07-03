package com.github.ren.file.server.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分片上传检测返回
 * @Author ren
 * @Since 1.0
 */
@ApiModel("文件检测结果")
@Data
public class CheckResult implements Serializable {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty("是否存在 true:存在(秒传) false:不存在")
//    private boolean exist;

    @ApiModelProperty("已经上传的分片数据 断点续传客户端可以跳过这些分片")
    private List<PartResult> parts;
}
