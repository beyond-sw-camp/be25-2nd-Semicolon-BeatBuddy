package com.beyond.beatbuddy.user.service;

import com.beyond.beatbuddy.user.dto.response.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserGroupNicknameListResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserNotificationSettingResponseDto;
import com.beyond.beatbuddy.user.dto.request.UpdateChatNotificationRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateSocialNotificationRequestDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateGroupNicknameRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateProfileImageRequestDto;

public interface UserService {
    UserProfileResponseDto getMyProfile(String email);

    UserNotificationSettingResponseDto getMyNotificationSetting(String email);

    void updateChatNotificationSetting(String email, UpdateChatNotificationRequestDto request);

    void updateSocialNotificationSetting(String email, UpdateSocialNotificationRequestDto request);

    UserGroupNicknameListResponseDto getMyGroupNicknames(String email);

    void changePassword(String email, ChangePasswordRequestDto request);

    void updateGroupNickname(String email, Long groupId, UpdateGroupNicknameRequestDto request);

    void updateProfileImage(String email, UpdateProfileImageRequestDto request);

    void withdraw(String email);
}
