package com.ren.file.util;

import com.ren.file.enmus.RErrorEnum;

import java.io.Serializable;

/**
 * 返回值封装处理
 *
 * @param <T>
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    private R() {
    }

    private R(T data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
    }

    private R(RErrorEnum errorEnum) {
        if (errorEnum == null) {
            return;
        }
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
    }

    private R(RErrorEnum errorEnum, T data) {
        if (errorEnum == null) {
            return;
        }
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
        this.data = data;
    }

    /**
     * 成功时
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> success(T data) {
        return new R<T>(data);
    }

    /**
     * 失败
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(RErrorEnum errorEnum) {
        return new R<T>(errorEnum);
    }

    public static <T> R<T> fail(RErrorEnum errorEnum, T data) {
        return new R<T>(errorEnum, data);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}