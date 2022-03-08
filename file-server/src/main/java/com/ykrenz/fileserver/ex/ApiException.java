package com.ykrenz.fileserver.ex;

import com.ykrenz.fileserver.model.ErrorCode;

/**
 * @author Mr Ren
 * @Description: api异常
 * @date 2021/4/7 10:57
 */
public class ApiException extends RuntimeException {

    protected int code;
    protected Object data;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
