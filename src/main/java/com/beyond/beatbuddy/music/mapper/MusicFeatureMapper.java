package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.dto.response.MusicSearchResponse;
import com.beyond.beatbuddy.music.entity.MusicFeature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MusicFeatureMapper {
    void insertMusicFeature(MusicFeature musicFeature);

    MusicFeature findById(String musicId);

    List<MusicSearchResponse> searchByKeyword(@Param("keyword") String keyword);
}
