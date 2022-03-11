package com.ykrenz.fileserver.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 分片上传检测返回
 * @Author ren
 * @Since 1.0
 */
@ApiModel(value = "InitMultipartResult", description = "初始化分片上传结果")
@Data
public class InitMultipartResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上传文件唯一标识")
    private String uploadId;

    @ApiModelProperty("是否存在 true:存在(秒传) false:不存在")
    private boolean exist;

    @ApiModelProperty("已经上传的分片数据 断点续传客户端可以跳过这些分片")
    private List<Integer> parts = new ArrayList<>(0);

    public InitMultipartResult() {
    }

    public InitMultipartResult(String uploadId, boolean exist) {
        this.uploadId = uploadId;
        this.exist = exist;
    }

    public InitMultipartResult(String uploadId, List<Integer> parts) {
        this.uploadId = uploadId;
        this.parts = parts;
    }
}
