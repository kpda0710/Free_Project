package com.project.free.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorResult {

    SUCCESS("1000", "SUCCESS"),
    USER_NOT_FOUND("2001", "USER NOT FOUND"),
    USER_NOT_MATCH_EMAIL("2002", "USER NOT MATCH EMAIL"),
    USER_NOT_MATCH_PASSWORD("2003", "USER NOT MATCH PASSWORD"),
    BOARD_NOT_FOUND("3001", "BOARD NOT FOUND"),
    BOARD_USERID_NOT_MATCH("3002", "BOARD USERID NOT MATCH"),
    COMMENT_NOT_FOUND("4001", "COMMENT NOT FOUND"),
    LIKES_NOT_FOUND("5001", "LIKES NOT FOUND"),
    LIKES_DUPLICATE("5002", "LIKES DUPLICATE"),
    FILE_SAVE_ERROR("6001", "FILE SAVE ERROR"),
    SYSTEM_ERROR("9000", "SYSTEM ERROR"),
    ;

    private final String code;

    private final String message;
}
