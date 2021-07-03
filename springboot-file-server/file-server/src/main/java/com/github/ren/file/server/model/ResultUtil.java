package com.github.ren.file.server.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 返回值封装处理
 *
 * @param <T>
 */
@Setter
@Getter
public class ResultUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private T data;
    private String message;

    private ResultUtil(T data) {
        this.code = 200;
        this.data = data;
        this.message = "success";
    }

    private ResultUtil(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    private ResultUtil(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.data = data;
        this.message = errorCode.getMessage();
    }

    private ResultUtil(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }

    /**
     * 成功时
     *
     * @param <T>
     * @return
     */
    public static <T> ResultUtil<T> success(T data) {
        return new ResultUtil<>(data);
    }

    /**
     * 成功时
     *
     * @param <T>
     * @return
     */
    public static <T> ResultUtil<T> success() {
        return new ResultUtil<>(null);
    }

    /**
     * 失败
     *
     * @param <T>
     * @return
     */
    public static <T> ResultUtil<T> error(ErrorCode errorCode) {
        return new ResultUtil<>(errorCode);
    }

    public static <T> ResultUtil<T> error(ErrorCode errorCode, T data) {
        return new ResultUtil<>(errorCode, data);
    }

    public static <T> ResultUtil<T> error(int code, String msg) {
        return new ResultUtil<>(code, null, msg);
    }

    public static <T> ResultUtil<T> error(int code, T data, String msg) {
        return new ResultUtil<>(code, data, msg);
    }

}