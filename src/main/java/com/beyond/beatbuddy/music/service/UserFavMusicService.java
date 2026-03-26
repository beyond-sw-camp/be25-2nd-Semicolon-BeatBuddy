package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.music.dto.FavMusicAddRequestDto;
import com.beyond.beatbuddy.music.dto.FavMusicResponseDto;
import com.beyond.beatbuddy.music.entity.MusicFeature;
import com.beyond.beatbuddy.music.entity.UserFavMusic;
import com.beyond.beatbuddy.music.mapper.MusicFeatureMapper;
import com.beyond.beatbuddy.music.mapper.UserFavMusicMapper;
import com.beyond.beatbuddy.taste.service.TasteAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavMusicService {

    private static final int MAX_FAV_COUNT = 10;

    private final UserFavMusicMapper userFavMusicMapper;
    private final MusicFeatureMapper musicFeatureMapper;
    private final SpotifyMusicService spotifyMusicService;
    private final TasteAnalysisService tasteAnalysisService;

    /**
     * 최애곡 추가 (중복 방지, 최대 10곡 제한)
     */
    @Transactional
    public void addFav(Long userId, FavMusicAddRequestDto request) {
        String musicId = request.getMusicId();

        // 1) 현재 저장 수 확인
        int count = userFavMusicMapper.countByUserId(userId);
        if (count >= MAX_FAV_COUNT) {
            throw new IllegalStateException("최애곡은 최대 " + MAX_FAV_COUNT + "곡까지 저장 가능합니다.");
        }

        // 2) music_features에 없으면 Spotify에서 동기화
        MusicFeature existing = musicFeatureMapper.findById(musicId);
        if (existing == null) {
            spotifyMusicService.syncMusicFeature(musicId);
        }

        // 3) 최애곡 저장 (DB UNIQUE KEY로 중복 방지)
        UserFavMusic userFavMusic = UserFavMusic.builder()
                .userId(userId)
                .musicId(musicId)
                .build();
        userFavMusicMapper.insert(userFavMusic);

        log.info("[FavMusic] 추가 완료: userId={}, musicId={}", userId, musicId);
    }

    /**
     * 최애곡 삭제
     */
    @Transactional
    public void deleteFav(Long userId, String musicId) {
        userFavMusicMapper.deleteByUserIdAndMusicId(userId, musicId);
        log.info("[FavMusic] 삭제 완료: userId={}, musicId={}", userId, musicId);
    }

    /**
     * 최애곡 목록 조회 (음악 정보 포함)
     */
    public List<FavMusicResponseDto> getFavList(Long userId) {
        List<UserFavMusic> favList = userFavMusicMapper.findByUserId(userId);

        return favList.stream().map(fav -> {
            MusicFeature mf = musicFeatureMapper.findById(fav.getMusicId());
            if (mf == null) {
                return FavMusicResponseDto.builder()
                        .musicId(fav.getMusicId())
                        .createdAt(fav.getCreatedAt())
                        .build();
            }
            return FavMusicResponseDto.builder()
                    .musicId(mf.getMusicId())
                    .trackName(mf.getTrackName())
                    .artistName(mf.getArtistName())
                    .createdAt(fav.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 현재 저장된 최애곡 수 조회
     */
    public int getFavCount(Long userId) {
        return userFavMusicMapper.countByUserId(userId);
    }

    /**
     * 10곡 확정 → 취향 분석 실행
     */
    @Transactional
    public void confirmAndAnalyze(Long userId) {
        int count = userFavMusicMapper.countByUserId(userId);
        if (count < MAX_FAV_COUNT) {
            throw new IllegalStateException("최애곡 " + MAX_FAV_COUNT + "곡을 모두 선택해야 취향 분석이 가능합니다. (현재 " + count + "곡)");
        }
        tasteAnalysisService.analyze(userId);
        log.info("[FavMusic] 취향 분석 완료: userId={}", userId);
    }
}
