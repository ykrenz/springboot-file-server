package com.ykrenz.file.model;

import lombok.Getter;

/**
 * @Description: 异常枚举类
 * @date 2020/5/22 14:40
 */
@Getter
public enum SystemErrorMessage {
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
    FILE_TO_LARGE("文件过大"),
    ;
    /**
     * 返回消息
     */
    private final String message;

    SystemErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
