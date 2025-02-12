package com.project.free.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private String code;

    private String message;

    private String extraMessage;

    public BaseException(ErrorResult errorResult) {
        super(errorResult.getMessage());
        this.code = errorResult.getCode();
        this.message = errorResult.getMessage();
        this.extraMessage = "Null";
    }

    public BaseException(ErrorResult errorResult, String extraMessage) {
        super(errorResult.getMessage() + " - " + extraMessage);
        this.code = errorResult.getCode();
        this.message = errorResult.getMessage();
        this.extraMessage = extraMessage;
    }
}
