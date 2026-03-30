package com.beyond.beatbuddy.user.service;

import com.beyond.beatbuddy.user.dto.response.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserGroupNicknameListResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateProfileImageRequestDto;

public interface UserService {
    UserProfileResponseDto getMyProfile(String email);

    UserGroupNicknameListResponseDto getMyGroupNicknames(String email);

    void changePassword(String email, ChangePasswordRequestDto request);

    void updateProfileImage(String email, UpdateProfileImageRequestDto request);

    void withdraw(String email);
}
