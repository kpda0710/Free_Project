package com.project.free.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("1000", "SUCCESS"),

    USER_CREATE_SUCCESS("2201", "유저 생성 성공"),
    USER_LOGIN_SUCCESS("2202", "유저 로그인 성공"),
    USER_AUTHORIZATION_SUCCESS("2203", "유저 인증 성공"),
    USER_GET_SUCCESS("2204", "유저 정보 가져오기 성공"),
    USER_UPDATE_SUCCESS("2205", "유저 업데이트 성공"),
    USER_DELETE_SUCCESS("2206", "유저 삭제 성공"),
    USER_NOT_FOUND("2401", "유저 정보를 찾을 수 없습니다."),
    USER_NOT_PASSWORD("2402", "비밀번호가 일치하지 않습니다."),

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
