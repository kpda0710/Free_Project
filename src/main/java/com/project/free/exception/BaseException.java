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

    public BaseException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.extraMessage = "Null";
    }

    public BaseException(ResponseCode responseCode, String extraMessage) {
        super(responseCode.getMessage() + " - " + extraMessage);
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.extraMessage = extraMessage;
    }
}
