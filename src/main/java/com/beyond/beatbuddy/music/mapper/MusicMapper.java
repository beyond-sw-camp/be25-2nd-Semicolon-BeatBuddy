package com.beyond.beatbuddy.music.mapper;

import com.beyond.beatbuddy.music.dto.response.TasteResponse;
import com.beyond.beatbuddy.music.entity.Album;
import com.beyond.beatbuddy.music.entity.MusicFeature;
import com.beyond.beatbuddy.music.entity.UserFavMusic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// Service가 DB 작업을 요청
@Mapper
public interface MusicMapper {

    void upsertAlbum(Album album);  // Spotify에서 가져온 앨범 정보 저장

    void upsertMusicFeature(MusicFeature musicFeature);  // 곡 기본 정보 + Rapid API 속성갑 저장

    void insertUserFavMusic(UserFavMusic userFavMusic);  // 유저가 선택한 곡

    void deleteUserFavMusicByUserId(@Param("userId") Long userId);  // 수정할 때 기존 10곡을 통째로 지우고 다시 저장

    List<TasteResponse.TrackInfo> findTasteTracksByUserId(@Param("userId") Long userId);  // 이미 저장된 취향 10곡 조회
}
