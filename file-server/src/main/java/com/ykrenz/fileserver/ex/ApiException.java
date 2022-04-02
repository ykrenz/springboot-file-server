package com.ykrenz.fileserver.ex;

import com.ykrenz.fileserver.model.ErrorCode;
import lombok.Getter;

/**
 * @author Mr Ren
 * @Description: api异常
 * @date 2021/4/7 10:57
 */
@Getter
public class ApiException extends RuntimeException {

    private final String error;

    private boolean reset;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.error = errorCode.getMessage();
    }

    public ApiException(ErrorCode errorCode, boolean reset) {
        super(errorCode.getMessage());
        this.error = errorCode.getMessage();
        this.reset = reset;
    }

    public ApiException(String error) {
        super(error);
        this.error = error;
    }

    public ApiException(String error, boolean reset) {
        super(error);
        this.error = error;
        this.reset = reset;
    }
}
