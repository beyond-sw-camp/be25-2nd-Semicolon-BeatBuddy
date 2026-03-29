package com.beyond.beatbuddy.user.mapper;

import com.beyond.beatbuddy.user.entity.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    Optional<User> selectUserByEmail(@Param("email") String email);

    void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    void withdrawUser(@Param("userId") Long userId);
}