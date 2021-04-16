package com.github.ren.file.model.result;

import com.github.ren.file.chunk.Chunk;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Mr Ren
 * @Description: 分片上传响应结果
 * @date 2021/4/11 0:33
 */
@Data
@AllArgsConstructor
@ApiModel("分片上传响应结果")
public class ChunkUploadResult {

    @ApiModelProperty("文件md5值")
    private String md5;

    @ApiModelProperty("上传成功分片信息")
    private Chunk chunk;
}
