package com.github.ren.file.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Mr Ren
 * @Description: 分块检测
 * @date 2021/4/1 14:25
 */
@Data
public class CheckChunkRequest {
    /**
     * 文件标识
     */
    @NotNull(message = "identifier不能为空")
    private String identifier;

    /**
     * 总块数
     */
    @NotNull(message = "totalChunks不能为空")
    private Integer totalChunks;

    /**
     * 总大小
     */
    @NotNull(message = "totalSize总大小不能为空")
    private Long totalSize;
}
