package com.beyond.beatbuddy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "현재 비밀번호가 올바르지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8~16자 영문/숫자/특수문자를 포함해야 합니다."),
    PROFILE_IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "이미지를 입력해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 토큰이 없거나 만료되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 점검 중입니다. 관리자에게 문의하세요.");

    private final HttpStatus status;
    private final String message;
}
