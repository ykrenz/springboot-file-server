package com.ykrenz.file.model;

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
@ApiModel(value = "响应", description = "响应数据")
public class ResultUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成功")
    private boolean success;
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("错误信息")
    private String error;
    @ApiModelProperty("错误码")
    private Integer errno;
    @ApiModelProperty("重置标识 分片上传时客户端可根据该标识重置上传")
    private Boolean reset;


    private ResultUtil(T data) {
        this.data = data;
        this.success = true;
    }

    public ResultUtil(int errno, String error, Boolean reset) {
        this.errno = errno;
        this.error = error;
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
    public static <T> ResultUtil<T> error(String error) {
        return new ResultUtil<>(500, error, false);
    }

    public static <T> ResultUtil<T> error(SystemErrorMessage error) {
        return new ResultUtil<>(500, error.getMessage(), false);
    }

    public static <T> ResultUtil<T> error(BizErrorMessage message) {
        return error(message, false);
    }

    public static <T> ResultUtil<T> error(BizErrorMessage message, boolean reset) {
        return new ResultUtil<>(message.getCode(), message.getMessage(), reset);
    }
}