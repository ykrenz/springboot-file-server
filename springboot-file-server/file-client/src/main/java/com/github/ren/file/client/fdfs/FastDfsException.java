package com.github.ren.file.client.fdfs;

import com.github.ren.file.client.ex.ClientException;

/**
 * @Description 客户端异常
 * @Author ren
 * @Since 1.0
 */
public class FastDfsException extends ClientException {
    public FastDfsException() {
        super();
    }

    public FastDfsException(String message) {
        super(message);
    }

    public FastDfsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastDfsException(Throwable cause) {
        super(cause);
    }

    protected FastDfsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
