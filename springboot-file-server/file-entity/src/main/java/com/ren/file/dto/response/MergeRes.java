package com.ren.file.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:检测分片响应
 * @date 2020/6/8 20:15
 */
@Data
public class MergeRes {
    /**
     * 该文件是否存在 秒传
     */
    private Boolean uploaded;

    /**
     * 是否需要合并
     */
    private Boolean merge;

    /**
     * 已经上传的分块数据
     */
    private List<Integer> chunkNumbers = new ArrayList<>();
}
