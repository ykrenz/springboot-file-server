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
     * 服务异常
     */
    SERVER_ERROR(500, "服务器内部异常,请联系管理员"),
    /**
     * 参数异常
     */
    PARAM_ERROR(501, "参数异常"),
    /**
     * 不支持当前请求方法
     */
    HTTP_METHOD_ERROR(502, "不支持当前请求方法"),
    /**
     * 不支持当前媒体类型
     */
    HTTP_MEDIA_ERROR(503, "不支持当前媒体类型"),

    /**
     * 上传失败
     */
    UPLOAD_ERROR(10000, "上传失败,请重试"),

    /**
     * uploadId不存在
     */
    UPLOAD_ID_NOT_FOUND(10001, "uploadId不存在或者已经过期"),

    /**
     * 文件过大
     */
    FILE_TO_LARGE(20001, "文件大于5M,请使用分片上传"),

    /**
     * 分片文件
     */
    FILE_PART_SIZE_ERROR(20002, "分片文件必须在5~100M之间"),
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
