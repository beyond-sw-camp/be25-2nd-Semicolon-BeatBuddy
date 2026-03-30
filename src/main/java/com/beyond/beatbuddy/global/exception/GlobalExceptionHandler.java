package com.beyond.beatbuddy.global.exception;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException e) {
		return ApiResponse.of(e.getStatus(), e.getMessage(), null);
	}

	// @Valid 검증 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidException(MethodArgumentNotValidException e) {
		String message = e.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();
		return ApiResponse.of(HttpStatus.BAD_REQUEST, message, null);
	}

	// @RequestParam 검증 실패
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
		String message = e.getConstraintViolations()
				.iterator()
				.next()
				.getMessage();
		return ApiResponse.of(HttpStatus.BAD_REQUEST, message, null);
	}

	// 나머지 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", null);
	}
}