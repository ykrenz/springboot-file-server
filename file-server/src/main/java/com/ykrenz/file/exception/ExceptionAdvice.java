package com.ykrenz.file.exception;

import com.ykrenz.file.model.SystemErrorMessage;
import com.ykrenz.file.model.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResultUtil.error(SystemErrorMessage.HTTP_METHOD_ERROR);
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResultUtil<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResultUtil.error(SystemErrorMessage.HTTP_MEDIA_ERROR);
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
        String message = SystemErrorMessage.PARAM_ERROR.getMessage();
        return ResultUtil.error(message + msg);
    }

    /**
     * 服务器内部异常处理
     */
    private ResultUtil<String> errorServerHandler(Exception e) {
        log.error("服务异常::", e);
        return ResultUtil.error(SystemErrorMessage.SERVER_ERROR);
    }

    /**
     * 业务异常直接抛给客户端
     */
    @ExceptionHandler(BizException.class)
    public ResultUtil<Object> bizException(BizException e) {
        return ResultUtil.error(e.getErrorMessage(), e.isReset());
    }
}
