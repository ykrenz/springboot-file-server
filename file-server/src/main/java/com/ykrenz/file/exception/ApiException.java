package com.ykrenz.file.exception;

import com.ykrenz.file.model.ApiErrorMessage;
import lombok.Getter;

/**
 * @author Mr Ren
 * @Description: api异常
 * @date 2021/4/7 10:57
 */
@Getter
public class ApiException extends RuntimeException {

    private final String error;

    private Boolean reset;

    public ApiException(ApiErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.error = errorMessage.getMessage();
    }

    public ApiException(ApiErrorMessage errorMessage, Boolean reset) {
        super(errorMessage.getMessage());
        this.error = errorMessage.getMessage();
        this.reset = reset;
    }

    public ApiException(String error) {
        super(error);
        this.error = error;
    }

    public ApiException(String error, Boolean reset) {
        super(error);
        this.error = error;
        this.reset = reset;
    }
}
