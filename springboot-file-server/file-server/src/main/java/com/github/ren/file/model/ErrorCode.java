package com.github.ren.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: 异常枚举类
 * @date 2020/5/22 14:40
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {

    /**
     * 上传失败
     */
    UPLOAD_ERROR(10000, "上传失败，请重试"),
    /**
     * 分片检测失败
     */
    UPLOAD_CHUNK_CHECK_ERROR(10001, "分片检测错误，请重试"),
    /**
     * 上传分片失败
     */
    UPLOAD_CHUNK_ERROR(10002, "上传分片失败，请重试"),
    /**
     * 合并文件失败
     */
    UPLOAD_MERGE_ERROR(10002, "合并分片失败，请重试"),
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
