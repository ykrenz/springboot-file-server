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
    SERVER_ERROR(500, "服务器异常,请联系管理员"),
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
    UPLOAD_ID_NOT_FOUND(10001, "uploadId不存在或已经过期"),

    /**
     * uploadId已经过期
     */
    UPLOAD_ID_EXPIRE(10002, "uploadId已经过期,请重新申请"),

    /**
     * 文件过大
     */
    FILE_TO_LARGE(20001, "文件大于5M,请使用分片上传"),

    /**
     * 分片文件
     */
    FILE_PART_SIZE_ERROR(20002, "分片文件大小有误"),

    /**
     * 分片文件数量为空
     */
    FILE_PART_ISEMPTY(20003, "分片文件数量为空"),

    /**
     * 分片数量有误
     */
    FILE_PART_COUNT_ERROR(20004, "分片数量有误"),

    /**
     * 分片文件数量为空
     */
    FILE_PART_TOTAL_SIZE_ERROR(20005, "分片总大小有误,不等于文件总大小"),
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
