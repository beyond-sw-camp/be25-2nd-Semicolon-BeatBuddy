package com.beyond.beatbuddy.user.controller;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.user.dto.response.UserGroupNicknameListResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateProfileImageRequestDto;
import com.beyond.beatbuddy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public UserProfileResponseDto getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getMyProfile(userDetails.getUsername());
    }

    @GetMapping("/me/group-nicknames")
    public UserGroupNicknameListResponseDto getMyGroupNicknames(
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getMyGroupNicknames(userDetails.getUsername());
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(userDetails.getUsername(), request);
        return ApiResponse.of(HttpStatus.OK, "비밀번호가 변경되었습니다.", null);
    }

    @PatchMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<Void>> updateProfileImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileImageRequestDto request) {
        // The client sends the selected image path/URL, and we persist that value for the current user.
        userService.updateProfileImage(userDetails.getUsername(), request);
        return ApiResponse.of(HttpStatus.OK, "프로필 사진이 변경되었습니다.", null);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.withdraw(userDetails.getUsername());
        return ApiResponse.of(HttpStatus.OK, "회원탈퇴가 완료되었습니다.", null);
    }
}

