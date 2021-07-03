package com.github.ren.file.ex;

import com.github.ren.file.model.ErrorCode;

/**
 * @author Mr Ren
 * @Description: UploadException
 * @date 2021/4/7 10:58
 */
public class UploadException extends ApiException {

    public UploadException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UploadException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
