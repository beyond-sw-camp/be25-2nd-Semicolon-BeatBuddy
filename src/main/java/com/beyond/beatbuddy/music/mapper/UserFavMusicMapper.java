package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.dto.response.FavMusicResponse;
import com.beyond.beatbuddy.music.dto.response.TasteVectorResponse;
import com.beyond.beatbuddy.music.entity.UserFavMusic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFavMusicMapper {
    void insertFavMusic(UserFavMusic userFavMusic);

    void deleteFavMusic(@Param("userId") Long userId, @Param("musicId") String musicId);

    int countFavMusic(Long userId);

    List<FavMusicResponse> findFavMusicByUserId(Long userId);

    TasteVectorResponse analyzeTasteVector(Long userId);
}
