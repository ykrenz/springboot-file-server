package com.github.ren.file.ex;

import com.github.ren.file.model.ErrorCode;

/**
 * @author Mr Ren
 * @Description: ChunkException
 * @date 2021/4/7 10:58
 */
public class ChunkException extends ApiException {

    public ChunkException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChunkException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
