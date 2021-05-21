package com.github.ren.file.sdk.ex;

/**
 * 本地文件读取异常
 */
public class FileIOException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param cause
     */
    public FileIOException(Throwable cause) {
        super("读取流异常出现了io异常", cause);
    }

    /**
     * @param message
     * @param cause
     */
    public FileIOException(String message, Throwable cause) {
        super("读取流异常出现了io异常:" + message, cause);
    }

    public FileIOException(String message) {
        super(message);
    }

}
