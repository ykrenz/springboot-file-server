package com.github.ren.file.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 返回值封装处理
 *
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ResultUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private T data;
    private String message;

    private ResultUtil() {
    }

    private ResultUtil(T data) {
        this.code = 200;
        this.data = data;
        this.message = "success";
    }

    private ResultUtil(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
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
     * 失败
     *
     * @param <T>
     * @return
     */
    public static <T> ResultUtil<T> error(ErrorCode errorCode) {
        return new ResultUtil<>(errorCode);
    }


}