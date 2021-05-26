package com.github.ren.file.ex;

import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * 异常通用处理
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 返回状态码:400
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultUtil<String> handleFieldError(Exception e) {
        StringBuilder builder = new StringBuilder();
        List<FieldError> fieldErrors = null;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            fieldErrors = ex.getBindingResult().getFieldErrors();
        } else {
            BindException ex = (BindException) e;
            fieldErrors = ex.getBindingResult().getFieldErrors();
        }
        for (FieldError fieldError : fieldErrors) {
            builder.append(fieldError.getField()).append(fieldError.getDefaultMessage());
        }
        return paramError(builder.toString());
    }

    /**
     * 返回状态码:400
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultUtil<String> handle(ValidationException e) {
        StringBuilder builder = new StringBuilder();
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                builder.append(item.getMessage());
            }
        } else {
            builder.append(e.getMessage());
        }
        return paramError(builder.toString());
    }

    /**
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResultUtil<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResultUtil.error(ErrorCode.HTTP_METHOD_ERROR);
    }

    /**
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MultipartException.class})
    public ResultUtil<String> multipartException(MultipartException e) {
        if (e instanceof MaxUploadSizeExceededException) {
            return ResultUtil.error(ErrorCode.FILE_TO_LARGE);
        }
        return ResultUtil.error(ErrorCode.HTTP_METHOD_ERROR);
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResultUtil<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResultUtil.error(ErrorCode.HTTP_MEDIA_ERROR);
    }

    /**
     * 业务异常直接抛给客户端
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ApiException.class)
    public ResultUtil<Object> apiException(ApiException e) {
        return ResultUtil.error(e.getCode(), e.getData(), e.getMessage());
    }

    /**
     * 服务器内部异常 返回处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResultUtil<String> handleException(Exception e) {
        return errorServerHandler(e);
    }

    /**
     * 参数异常
     */
    private ResultUtil<String> paramError(String msg) {
        String message = ErrorCode.PARAM_ERROR.getMessage();
        int code = ErrorCode.PARAM_ERROR.getCode();
        return ResultUtil.error(code, message + msg);
    }

    /**
     * 服务器内部异常处理
     */
    private ResultUtil<String> errorServerHandler(Exception e) {
        log.error("服务异常::", e);
        String stackTrace = ExceptionUtils.getStackTrace(e);
        return ResultUtil.error(ErrorCode.SERVER_ERROR, "异常信息:" + String.format("[%s]", stackTrace));
    }
}
