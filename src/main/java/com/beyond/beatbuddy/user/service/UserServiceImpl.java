package com.beyond.beatbuddy.user.service;


import com.beyond.beatbuddy.global.exception.CustomException;
import com.beyond.beatbuddy.global.exception.ErrorCode;
import com.beyond.beatbuddy.user.dto.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.entity.User;
import com.beyond.beatbuddy.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResponseDto getMyProfile(String email) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserProfileResponseDto(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDto request) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String encoded = passwordEncoder.encode(request.getNewPassword());
        userMapper.updatePassword(user.getUserId(), encoded);
    }
}
