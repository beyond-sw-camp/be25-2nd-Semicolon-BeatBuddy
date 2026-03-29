package com.beyond.beatbuddy.user.service;

import com.beyond.beatbuddy.user.dto.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;

public interface UserService {
    UserProfileResponseDto getMyProfile(String email);

    void changePassword(String email, ChangePasswordRequestDto request);

    void withdraw(String email);
}