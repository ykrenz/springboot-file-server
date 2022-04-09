package com.ykrenz.file.model;

import lombok.Getter;

/**
 * @Description: 异常枚举类
 * @date 2020/5/22 14:40
 */
@Getter
public enum ErrorCode {
    /**
     * 服务异常
     */
    SERVER_ERROR("服务器异常,请联系管理员"),
    /**
     * 参数异常
     */
    PARAM_ERROR("参数异常"),
    /**
     * 不支持当前请求方法
     */
    HTTP_METHOD_ERROR("不支持当前请求方法"),
    /**
     * 不支持当前媒体类型
     */
    HTTP_MEDIA_ERROR("不支持当前媒体类型"),
    /**
     * 文件过大
     */
    FILE_TO_LARGE("文件过大,请使用分片上传"),
    /**
     * uploadId不存在
     */
    UPLOAD_ID_NOT_FOUND("uploadId不存在或已经过期"),
    /**
     * crc校验失败
     */
    FILE_CRC_ERROR("crc校验失败,请重新上传"),
    /**
     * 文件不存在
     */
    FILE_NOT_FOUND("文件不存在"),
    ;
    /**
     * 返回消息
     */
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
