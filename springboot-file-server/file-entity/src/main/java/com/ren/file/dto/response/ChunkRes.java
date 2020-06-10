package com.ren.file.dto.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 分片上传响应
 * @date 2020/6/10 20:59
 */
@Data
public class ChunkRes implements Serializable {

    private Boolean merge;

    private String url;
}
