package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.entity.UserFavMusic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFavMusicMapper {

    void insert(UserFavMusic userFavMusic);

    void deleteByUserIdAndMusicId(@Param("userId") Long userId, @Param("musicId") String musicId);

    List<UserFavMusic> findByUserId(Long userId);

    int countByUserId(Long userId);
}
