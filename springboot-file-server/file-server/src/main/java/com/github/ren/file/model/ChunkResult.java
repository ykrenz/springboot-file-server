package com.github.ren.file.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: 分片上传结果
 * @date 2021/4/6 16:08
 */
@ApiModel("分片上传结果")
@Data
public class ChunkResult implements Serializable {
    private static final long serialVersionUID = 1L;

    public ChunkResult() {
    }

    public ChunkResult(FileUploadResult file) {
        this.file = file;
    }

    public ChunkResult(List<Long> chunkNumbers) {
        this.chunkNumbers = chunkNumbers;
    }

    public ChunkResult(FileUploadResult file, List<Long> chunkNumbers) {
        this.file = file;
        this.chunkNumbers = chunkNumbers;
    }

    /**
     * 已经上传的文件信息
     */
    @ApiModelProperty("是否已经上传 已经上传则file不为空")
    private FileUploadResult file;

    /**
     * 已经上传的分块数据
     */
    @ApiModelProperty("已经上传的分块数据 断点续传客户端可以跳过这些分片")
    private List<Long> chunkNumbers = new ArrayList<>(0);
}
