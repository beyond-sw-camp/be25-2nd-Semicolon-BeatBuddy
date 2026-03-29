package com.beyond.beatbuddy.auth.mapper;

import com.beyond.beatbuddy.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    User findByEmail(String email);

    User findById(Long userId);

    void updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    void deleteRefreshToken(Long userId);
}
