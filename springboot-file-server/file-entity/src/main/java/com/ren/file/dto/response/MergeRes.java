package com.ren.file.dto.response;

import com.ren.file.entity.Fileinfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:检测分片响应
 * @date 2020/6/8 20:15
 */
@Data
public class MergeRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 该文件是否存在 秒传
     */
    private Boolean uploaded;

    /**
     * 已经上传的文件信息
     */
    private Fileinfo fileinfo;

    /**
     * 是否需要合并
     */
    private Boolean merge;

    /**
     * 已经上传的分块数据
     */
    private List<Integer> chunkNumbers = new ArrayList<>();
}
