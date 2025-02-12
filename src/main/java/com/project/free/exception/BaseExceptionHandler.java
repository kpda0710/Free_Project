package com.project.free.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleExceptionBaseException(BaseException exception){
        return new ResponseEntity<>("Error Code: " + exception.getCode() + ", Error Message: " + exception.getMessage() + ", ExtraMessage: " + exception.getExtraMessage(), HttpStatus.BAD_REQUEST);
    }
}
