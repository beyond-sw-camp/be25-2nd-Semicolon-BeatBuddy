package com.beyond.beatbuddy.auth.controller;

import com.beyond.beatbuddy.auth.dto.request.LoginRequestDto;
import com.beyond.beatbuddy.auth.dto.request.SignupRequestDto;
import com.beyond.beatbuddy.auth.dto.response.LoginResponseDto;
import com.beyond.beatbuddy.auth.service.AuthService;
import com.beyond.beatbuddy.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** 이메일 중복 확인 (USER_002) */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkEmail(@RequestParam String email) {
        boolean duplicate = authService.isEmailDuplicate(email);
        return ApiResponse.of(HttpStatus.OK, "이메일 중복 확인 완료",
                Map.of("isDuplicate", duplicate));
    }

    /** 회원가입 (USER_002) */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ApiResponse.of(HttpStatus.CREATED, "회원가입 완료", null);
    }

    /** 로그인 (USER_006) */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ApiResponse.of(HttpStatus.OK, "로그인 성공", response);
    }

    /** 토큰 재발급 */
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<LoginResponseDto>> reissue(
            @RequestHeader("Refresh-Token") String refreshToken) {
        LoginResponseDto response = authService.reissue(refreshToken);
        return ApiResponse.of(HttpStatus.OK, "토큰 재발급 성공", response);
    }

    /** 로그아웃 (MYPAGE_006) */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        authService.logout(userId);
        return ApiResponse.of(HttpStatus.OK, "로그아웃 완료", null);
    }
}
