package com.beyond.beatbuddy.auth.controller;
import com.beyond.beatbuddy.auth.dto.request.EmailSendRequest;
import com.beyond.beatbuddy.auth.dto.request.EmailVerifyRequest;
import com.beyond.beatbuddy.auth.dto.request.LoginRequest;
import com.beyond.beatbuddy.auth.dto.request.SignupRequest;
import com.beyond.beatbuddy.auth.dto.response.EmailSendResponse;
import com.beyond.beatbuddy.auth.dto.response.EmailVerifyResponse;
import com.beyond.beatbuddy.auth.dto.response.TokenResponse;
import com.beyond.beatbuddy.auth.service.AuthService;
import com.beyond.beatbuddy.global.dto.ApiResponse;

import com.beyond.beatbuddy.global.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 회원가입
	@PostMapping(value = "/signup", consumes = "multipart/form-data")
	public ResponseEntity<?> signup(
			@RequestPart("data") @Valid SignupRequest request,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
			HttpServletResponse response) {

		TokenResponse tokenResponse = authService.signUp(request, profileImage);

		ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ApiResponse.of(HttpStatus.CREATED, "회원가입 성공",
				TokenResponse.builder()
						.accessToken(tokenResponse.getAccessToken())
						.userId(tokenResponse.getUserId())
						.email(tokenResponse.getEmail())
						.nickname(tokenResponse.getNickname())
						.build()
		);
	}

	@PostMapping("/email/send")
	public ResponseEntity<?> sendVerificationCode(
			@RequestBody @Valid EmailSendRequest request) {
		EmailSendResponse emailSendResponse = authService.sendVerificationCode(request.getEmail());
		return ApiResponse.of(HttpStatus.OK, "인증코드를 발송했습니다.", emailSendResponse);
	}


	@PostMapping("/email/verify")
	public ResponseEntity<?> verifyCode(
			@RequestBody @Valid EmailVerifyRequest request) {
		EmailVerifyResponse response = authService.verifyCode(request.getEmail(), request.getCode());
		return ApiResponse.of(HttpStatus.OK, "인증이 완료됐습니다.", response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(
			@RequestBody @Valid LoginRequest request,
			HttpServletResponse response) {

		TokenResponse tokenResponse = authService.login(request);

		// Refresh Token → HttpOnly Cookie
		ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ApiResponse.of(HttpStatus.OK, "로그인 성공",
				TokenResponse.builder()
						.accessToken(tokenResponse.getAccessToken())
						.userId(tokenResponse.getUserId())
						.email(tokenResponse.getEmail())
						.nickname(tokenResponse.getNickname())
						.build()
		);
	}

	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			@RequestHeader("Authorization") String bearerToken,
			HttpServletResponse response) {

		authService.logout(bearerToken);
		// Refresh Token 쿠키 삭제
		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
				.httpOnly(true)
				.path("/")
				.maxAge(0)  // 즉시 만료
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

		return ApiResponse.of(HttpStatus.OK, "로그아웃 성공", null);
	}

	// refresh
	@PostMapping("/token/refresh")
	public ResponseEntity<?> refreshToken(
			@CookieValue(name = "refreshToken", required = false)String refreshToken) {

		if (refreshToken == null) {
			throw new UnauthorizedException("Refresh Token이 없습니다.");
		}

		String newAccessToken = authService.refresh(refreshToken);

		return ApiResponse.of(HttpStatus.OK, "토큰 재발급 성공",
				Map.of("accessToken", newAccessToken)
		);
	}

	// 쿠키 생성 공통 메서드
	private ResponseCookie createRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from("refreshToken", refreshToken)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(Duration.ofDays(7))
				.sameSite("Strict")
				.build();
	}
}