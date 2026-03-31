package com.beyond.beatbuddy.user.service;

import com.beyond.beatbuddy.global.exception.CustomException;
import com.beyond.beatbuddy.global.exception.ErrorCode;
import com.beyond.beatbuddy.user.dto.response.UserGroupNicknameListResponseDto;
import com.beyond.beatbuddy.user.dto.response.UserProfileResponseDto;
import com.beyond.beatbuddy.user.dto.request.ChangePasswordRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateGroupNicknameRequestDto;
import com.beyond.beatbuddy.user.dto.request.UpdateProfileImageRequestDto;
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
    public UserGroupNicknameListResponseDto getMyGroupNicknames(String email) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // Reuse the authenticated user's primary key to query group-specific nicknames.
        return new UserGroupNicknameListResponseDto(
                userMapper.selectMyGroupNicknames(user.getUserId())
        );
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

    @Override
    public void updateGroupNickname(String email, Long groupId, UpdateGroupNicknameRequestDto request) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // A group nickname must stay unique inside the same group, excluding the current user.
        int duplicateCount = userMapper.countDuplicateGroupNickname(
                groupId, user.getUserId(), request.getGroupNickname()
        );
        if (duplicateCount > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_GROUP_NICKNAME);
        }

        // If no row is updated, the user does not belong to that group.
        int updatedRows = userMapper.updateGroupNickname(
                user.getUserId(), groupId, request.getGroupNickname()
        );
        if (updatedRows == 0) {
            throw new CustomException(ErrorCode.GROUP_MEMBER_NOT_FOUND);
        }
    }

    @Override
    public void updateProfileImage(String email, UpdateProfileImageRequestDto request) {
        // Resolve the authenticated user first, then update only that user's profile image path.
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateProfileImage(user.getUserId(), request.getProfileImageUrl());
    }

    @Override
    public void withdraw(String email) {
        User user = userMapper.selectUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userMapper.withdrawUser(user.getUserId());
    }
}
