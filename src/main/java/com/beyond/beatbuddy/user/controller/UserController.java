package com.beyond.beatbuddy.user.controller;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.user.dto.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponseDto getMyProfile(@AuthenticationPrincipal UserDetails
                                                       userDetails) {
        return userService.getMyProfile(userDetails.getUsername());
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(userDetails.getUsername(), request);
        return ApiResponse.of(HttpStatus.OK, "비밀번호가 변경되었습니다.", null);
    }
}