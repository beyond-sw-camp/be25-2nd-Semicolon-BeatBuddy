package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.entity.MusicFeature;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicFeatureMapper {

    void insertMusicFeature(MusicFeature musicFeature);

    MusicFeature findById(String musicId);
}
