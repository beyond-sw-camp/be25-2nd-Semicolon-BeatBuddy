package com.beyond.beatbuddy.auth.controller;
import com.beyond.beatbuddy.auth.dto.request.LoginRequest;
import com.beyond.beatbuddy.auth.dto.request.SignupRequest;
import com.beyond.beatbuddy.auth.dto.response.LoginResponse;
import com.beyond.beatbuddy.auth.service.AuthService;
import com.beyond.beatbuddy.global.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 회원가입
	@PostMapping(value = "/signup", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Void>> signup(
			@RequestPart("data") @Valid SignupRequest request,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
		authService.signUp(request, profileImage);
		return ResponseEntity.ok(ApiResponse.builder().build());
	}

	// 로그인/api/v1/auth/login
	@PostMapping(value = "/login")
	public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
		LoginResponse loginResponse = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok()
						.headers()
			ApiResponse.builder().build());
	}



	// 로그아웃


	// refresh


	//
}