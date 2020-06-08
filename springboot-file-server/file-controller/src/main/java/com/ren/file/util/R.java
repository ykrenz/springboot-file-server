package com.ren.file.util;

import com.ren.file.enums.RErrorEnum;

import java.io.Serializable;

/**
 * 返回值封装处理
 *
 * @param <T>
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    private R() {
    }

    private R(T data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    private R(RErrorEnum errorEnum) {
        if (errorEnum == null) {
            return;
        }
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
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

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}