package com.beyond.beatbuddy.user.service;

import com.beyond.beatbuddy.user.dto.UserProfileResponseDto;

public interface UserService {
    UserProfileResponseDto getMyProfile(String email);
}