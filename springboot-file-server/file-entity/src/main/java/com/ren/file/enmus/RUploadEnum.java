package com.ren.file.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: 枚举类
 * @date 2020/5/22 14:40
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RUploadEnum {

    /**
     * 上传失败
     */
    UPLOAD_ERROR(200, "上传成功"),
    /**
     * 分片正在上传中
     */
    UPLOAD_CHUNK_PROCESS(20001, "分片正在上传中"),
    /**
     * 分片正在合并中
     */
    UPLOAD_CHUNK_NEED_MERGE(20002, "分片需要合并"),
    /**
     * 分片正在合并中
     */
    UPLOAD_CHUNK_MERGE(20001, "分片正在合并中"),
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
