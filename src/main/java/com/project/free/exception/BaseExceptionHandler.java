package com.project.free.exception;

import com.project.free.util.CustomResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected CustomResponse<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        return CustomResponse.fail(ResponseCode.FAIL_VALIDATION, exception.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(BaseException.class)
    protected CustomResponse<String> handleExceptionBaseException(BaseException exception){
        return CustomResponse.fail(ResponseCode.SYSTEM_ERROR, exception.getCode() + ", Error Message: " + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected CustomResponse<String> handleException(Exception exception){
        return CustomResponse.fail(ResponseCode.SYSTEM_ERROR, exception.getMessage());
    }
}
