package com.beyond.beatbuddy.user.controller;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.user.dto.request.UpdateChatNotificationRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateSocialNotificationRequestDto;
import com.beyond.beatbuddy.user.dto.response.UserGroupNicknameListResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserNotificationSettingResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserNotificationUpdateResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateGroupNicknameRequestDto;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/me/notification")
    public UserNotificationSettingResponseDto getMyNotificationSetting(
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getMyNotificationSetting(userDetails.getUsername());
    }

    @PatchMapping("/me/notifications/chat")
    public UserNotificationUpdateResponseDto updateChatNotificationSetting(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateChatNotificationRequestDto request) {
        userService.updateChatNotificationSetting(userDetails.getUsername(), request);
        return new UserNotificationUpdateResponseDto("채팅 알림 설정이 변경되었습니다.");
    }

    @PatchMapping("/me/notifications/social")
    public UserNotificationUpdateResponseDto updateSocialNotificationSetting(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateSocialNotificationRequestDto request) {
        userService.updateSocialNotificationSetting(userDetails.getUsername(), request);
        return new UserNotificationUpdateResponseDto("소셜 알림 설정이 변경되었습니다.");
    }

    @GetMapping("/me/group-nicknames")
    public UserGroupNicknameListResponseDto getMyGroupNicknames(
            @AuthenticationPrincipal UserDetails userDetails) {
        // Look up all groups the authenticated user belongs to and return each group nickname.
        return userService.getMyGroupNicknames(userDetails.getUsername());
    }

    @PatchMapping("/me/group-nicknames/{groupId}")
    public ResponseEntity<ApiResponse<Void>> updateGroupNickname(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @Valid @RequestBody UpdateGroupNicknameRequestDto request) {
        // Update only the nickname used by the authenticated user inside the specified group.
        userService.updateGroupNickname(userDetails.getUsername(), groupId, request);
        return ApiResponse.of(HttpStatus.OK, "그룹 닉네임이 변경되었습니다.", null);
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

