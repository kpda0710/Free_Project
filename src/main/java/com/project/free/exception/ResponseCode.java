package com.project.free.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("1000", "성공!!"),
    USER_NOT_FOUND("2401", "유저 정보를 찾을 수 없습니다."),
    USER_NOT_PASSWORD("2402", "비밀번호가 일치하지 않습니다."),
    USER_SHOPPING_NOT_FOUND("2403", "유저 뱅킹 정보가 없습니다"),

    BOARD_NOT_FOUND("3001", "게시판이 존재하지 않습니다."),
    BOARD_USERID_NOT_MATCH("3002", "게시판과 유저 정보가 일치하지 않습니다."),

    COMMENT_NOT_FOUND("4001", "댓글을 찾을 수 없습니다."),

    LIKES_NOT_FOUND("5001", "좋아요를 찾을 수 없습니다."),
    LIKES_DUPLICATE("5002", "이미 좋아요를 눌렀습니다."),

    FILE_SAVE_ERROR("6001", "파일 저장 중 오류가 발생했습니다."),
    FILE_NOT_FOUND("6002", "파일을 찾을 수 없습니다."),

    ITEM_NOT_FOUND("7002", "상품을 찾을 수 없습니다."),
    ITEM_BUY_FAIL("7003", "잔고가 부족합니다."),

    SYSTEM_ERROR("9000", "시스템 에러가 발생했습니다."),
    FAIL_VALIDATION("9001", "벨리데이션 체크 실패")
    ;

    private final String code;

    private final String message;
}
