package com.ykrenz.file.model;

import lombok.Getter;

/**
 * @Description: 异常枚举类
 * @date 2020/5/22 14:40
 */
@Getter
public enum ApiErrorMessage {
    /**
     * uploadId不存在
     */
    UPLOAD_ID_NOT_FOUND("uploadId不存在或已经过期"),
    /**
     * crc校验失败
     */
    FILE_CRC_ERROR("crc校验失败"),
    /**
     * 文件不存在
     */
    FILE_NOT_FOUND("文件不存在"),
    ;
    /**
     * 返回消息
     */
    private final String message;

    ApiErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
