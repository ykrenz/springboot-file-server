package com.github.ren.file.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: 文件检测结果
 * @date 2021/4/14 22:30
 */
@ApiModel("文件检测结果")
@Data
public class CheckFileResult implements Serializable {
    public CheckFileResult(boolean exist, FileUploadResult file) {
        this.file = file;
        this.exist = exist;
    }

    public CheckFileResult(boolean exist, List<Integer> chunkNumbers) {
        this.exist = exist;
        this.chunkNumbers = chunkNumbers;
    }

    public CheckFileResult(boolean exist, FileUploadResult file, List<Integer> chunkNumbers) {
        this.file = file;
        this.exist = exist;
        this.chunkNumbers = chunkNumbers;
    }

    @ApiModelProperty("是否存在 true:存在(秒传) false:不存在")
    private boolean exist;

    @ApiModelProperty("文件信息")
    private FileUploadResult file;

    @ApiModelProperty("已经上传的分块数据 断点续传客户端可以跳过这些分片")
    private List<Integer> chunkNumbers = new ArrayList<>(0);
}
