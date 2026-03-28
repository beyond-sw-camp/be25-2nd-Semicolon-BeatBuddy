package com.beyond.beatbuddy.user.service;

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
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return new UserProfileResponseDto(user);
    }
}
