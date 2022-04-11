package com.ykrenz.file.model;

import lombok.Getter;

/**
 * @Description: 异常枚举类
 * @date 2020/5/22 14:40
 */
@Getter
public enum BizErrorMessage {
    /**
     * uploadId不存在
     */
    UPLOAD_ID_NOT_FOUND(404, "uploadId不存在或已经过期"),
    /**
     * crc校验失败
     */
    CHECK_HASH_ERROR(400, "hash校验失败"),
    /**
     * 文件不存在
     */
    NOT_FOUND(404, "文件不存在"),
    /**
     * 分片大小错误
     */
    PART_SIZE_ERROR(400, "分片大小错误"),
    /**
     * 服务器不支持
     */
    NOT_SUPPORT(403, "服务器不支持"),
    ;

    private final int code;
    /**
     * 返回消息
     */
    private final String message;

    BizErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
