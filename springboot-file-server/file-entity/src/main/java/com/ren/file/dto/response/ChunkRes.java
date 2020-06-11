package com.ren.file.dto.response;

import com.ren.file.entity.Fileinfo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 分片上传响应
 * @date 2020/6/10 20:59
 */
@Data
@Builder
public class ChunkRes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean merge;

    private Fileinfo fileinfo;
}
