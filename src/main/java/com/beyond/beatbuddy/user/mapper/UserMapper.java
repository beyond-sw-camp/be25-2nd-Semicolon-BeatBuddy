package com.beyond.beatbuddy.user.mapper;

import com.beyond.beatbuddy.user.dto.response.UserGroupNicknameItemResponseDto;
import com.beyond.beatbuddy.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    Optional<User> selectUserByEmail(@Param("email") String email);

    List<UserGroupNicknameItemResponseDto> selectMyGroupNicknames(@Param("userId") Long userId);

    void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    void updateProfileImage(@Param("userId") Long userId, @Param("profileImageUrl") String profileImageUrl);

    void withdrawUser(@Param("userId") Long userId);
}
