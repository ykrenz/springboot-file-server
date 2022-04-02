package com.ykrenz.fileserver.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Result", description = "响应结果")
public class ResultUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成功")
    private boolean success;
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("错误信息")
    private String error;
    @ApiModelProperty("重置分片上传")
    private boolean reset;


    private ResultUtil(T data) {
        this.data = data;
        this.success = true;
    }

    private ResultUtil(String error, boolean reset) {
        this.error = error;
        this.success = false;
        this.reset = reset;
    }

    /**
     * success
     *
     * @param <T>
     * @return
     */
    public static <T> ResultUtil<T> success(T data) {
        return new ResultUtil<>(data);
    }

    /**
     * success
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
    public static <T> ResultUtil<T> error(ErrorCode error) {
        return new ResultUtil<>(error.getMessage(), false);
    }

    public static <T> ResultUtil<T> error(String error) {
        return new ResultUtil<>(error, false);
    }

    public static <T> ResultUtil<T> error(String error, boolean reset) {
        return new ResultUtil<>(error, reset);
    }
}