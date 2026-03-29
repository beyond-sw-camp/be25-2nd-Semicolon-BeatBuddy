package com.beyond.beatbuddy.user.service;

import com.beyond.beatbuddy.global.exception.CustomException;
import com.beyond.beatbuddy.global.exception.ErrorCode;
import com.beyond.beatbuddy.user.dto.UserProfileResponseDto;
import com.beyond.beatbuddy.user.entity.User;
import com.beyond.beatbuddy.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserProfileResponseDto getMyProfile(String email) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserProfileResponseDto(user);
    }
}
