package com.github.ren.file.model;

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
    private boolean success;
    private T data;
    private String error;


    private ResultUtil(T data) {
        this.data = data;
        this.success = true;
        this.code = 200;
    }

    private ResultUtil(ErrorCode errorCode, T data) {
        this.data = data;
        this.error = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    private ResultUtil(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.error = msg;
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
     * @param
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
        return new ResultUtil<>(errorCode, null);
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