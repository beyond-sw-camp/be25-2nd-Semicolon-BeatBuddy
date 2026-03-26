package com.beyond.beatbuddy.global.exception;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException e) {
        log.warn("[Exception] {}", e.getMessage());
        return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("[Exception] {}", e.getMessage());
        return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntime(RuntimeException e) {
        log.error("[Exception] {}", e.getMessage(), e);
        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", null);
    }
}
