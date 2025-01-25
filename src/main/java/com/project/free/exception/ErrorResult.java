package com.project.free.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorResult {

    SUCCESS("1000", "SUCCESS"),
    USER_NOT_FOUND("4001", "USER NOT FOUND"),
    BOARD_NOT_FOUND("4002", "BOARD NOT FOUND"),
    COMMENT_NOT_FOUND("4003", "COMMENT NOT FOUND"),
    LIKES_NOT_FOUND("4004", "LIKES NOT FOUND"),
    LIKES_DUPLICATE("5001", "LIKES DUPLICATE"),
    SYSTEM_ERROR("9000", "SYSTEM ERROR"),
    ;

    private final String code;

    private final String message;
}
