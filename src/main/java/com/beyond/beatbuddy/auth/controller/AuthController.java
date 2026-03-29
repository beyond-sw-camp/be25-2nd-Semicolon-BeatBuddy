package com.beyond.beatbuddy.auth.controller;
import com.beyond.beatbuddy.auth.dto.request.EmailSendRequest;
import com.beyond.beatbuddy.auth.dto.request.LoginRequest;
import com.beyond.beatbuddy.auth.dto.request.SignupRequest;
import com.beyond.beatbuddy.auth.dto.response.EmailSendResponse;
import com.beyond.beatbuddy.auth.dto.response.TokenResponse;
import com.beyond.beatbuddy.auth.service.AuthService;
import com.beyond.beatbuddy.global.dto.ApiResponse;

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

		ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(Duration.ofDays(7))
				.sameSite("Strict")
				.build();
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


	// 로그인/api/v1/auth/login
	//	@PostMapping(value = "/login")
	//	public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
	//		LoginResponse loginResponse = authService.login(request.getEmail(), request.getPassword());
	//		return ResponseEntity.ok()
	//						.headers()
	//			ApiResponse.builder().build());
	//	}



	// 로그아웃


	// refresh


	//
}