package com.beyond.beatbuddy.taste.mapper;

import com.beyond.beatbuddy.taste.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {

    void upsert(@Param("userId") Long userId, @Param("tasteVectorStr") String tasteVectorStr);

    UserProfile findByUserId(Long userId);
}
