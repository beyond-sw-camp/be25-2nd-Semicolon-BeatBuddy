package com.beyond.beatbuddy.auth.controller;
import com.beyond.beatbuddy.auth.service.AuthService;
import com.beyond.beatbuddy.global.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
}