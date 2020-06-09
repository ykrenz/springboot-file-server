package com.ren.file.pojo.response;

import lombok.Data;

/**
 * @Description: 分块上传响应实体
 * @date 2020/6/9 20:07
 */
@Data
public class UploadChunkRes {
    /**
     * 当前上传操作是否需要合并
     */
    private Boolean merge;

    /**
     * 文件访问路径
     */
    private String url;
}
