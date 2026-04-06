package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.music.dto.request.MusicSearchRequest;
import com.beyond.beatbuddy.music.dto.request.TasteSaveRequest;
import com.beyond.beatbuddy.music.dto.response.MusicSearchResponse;
import com.beyond.beatbuddy.music.dto.response.TasteResponse;
import com.beyond.beatbuddy.music.dto.response.TasteSaveResponse;
import com.beyond.beatbuddy.music.entity.Album;
import com.beyond.beatbuddy.music.entity.MusicFeature;
import com.beyond.beatbuddy.music.entity.UserFavMusic;
import com.beyond.beatbuddy.music.mapper.MusicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 전체 음악 기능 흐름 담당
// 직접 Spotify를 모르고, SpotifyService에게 검색을 맡김

@Service
@RequiredArgsConstructor  // Spring 자동 주입
public class MusicService {

    private final MusicMapper musicMapper;
    private final SpotifyService spotifyService;  // SpotifyService 연결

    /*
    * 음악 검색
    *  - 검색어를 받아 SpotifyService에 검색을 요청
    */
    public List<MusicSearchResponse> searchMusic(MusicSearchRequest request) {
        return spotifyService.searchMusic(request.getKeyword());
    }

    /*
    * 최애곡 최초 저장
    *  - 프론트에서 받은 trackId 10개를 기준으로 Spotify 상세 조회
    *  - Rapid API 연결 전 임시 구조
    */
    @Transactional
    public TasteSaveResponse saveTaste(Long userId, TasteSaveRequest request) {
        validateTrackCount(request);

        int savedCount = 0;

        for (TasteSaveRequest.TrackRequest trackRequest : request.getTracks()) {
            String trackId = trackRequest.getTrackId();

            // 1. Spotify에서 실제 곡 상세 정보 조회
            MusicSearchResponse musicInfo = getTrackInfoFromSpotify(trackId);

            // 2. Rapid API는 아직 연결 전이라 임시 데이터 생성
            MusicFeature musicFeature = getTrackFeatureFromRapidApi(trackId, musicInfo);

            // 3. 앨범 저장
            Album album = Album.builder()
                    .albumId(musicInfo.getAlbumId())
                    .albumName(musicInfo.getAlbumName())
                    .albumCoverUrl(musicInfo.getAlbumCoverUrl())
                    .build();

            musicMapper.upsertAlbum(album);

            // 4. 곡 특성 저장
            musicMapper.upsertMusicFeature(musicFeature);

            // 5. 유저-최애곡 매핑 저장
            UserFavMusic userFavMusic = UserFavMusic.builder()
                    .userId(userId)
                    .musicId(trackId)
                    .build();

            musicMapper.insertUserFavMusic(userFavMusic);
            savedCount++;
        }

        return TasteSaveResponse.builder()
                .userId(userId)
                .savedCount(savedCount)
                .success(true)
                .message("최애곡 저장이 완료되었습니다.")
                .build();
    }

    /*
    * 최애곡 수정
    *  - 기존 최애곡 전체 삭제 후 새로 저장
    */
    @Transactional
    public TasteSaveResponse updateTaste(Long userId, TasteSaveRequest request) {
        validateTrackCount(request);

        musicMapper.deleteUserFavMusicByUserId(userId);

        return saveTaste(userId, request);
    }

    /*
    * 저장된 최애곡 조회
    */
    public TasteResponse getTaste(Long userId) {
        List<TasteResponse.TrackInfo> tracks = musicMapper.findTasteTracksByUserId(userId);

        return TasteResponse.builder()
                .tracks(tracks)
                .build();
    }

    /*
    * 10곡 검증
    */
    private void validateTrackCount(TasteSaveRequest request) {
        if (request.getTracks() == null || request.getTracks().size() != 10) {
            throw new IllegalArgumentException("최애곡 저장은 10곡만 가능합니다.");
        }
    }

    /*
    * Spotify 상세 정보
    *  - trackId로 Spotify 상세조회 연결
    */
    private MusicSearchResponse getTrackInfoFromSpotify(String trackId) {
        return spotifyService.getTrackInfo(trackId);
    }

    /*
    * 임시 Rapid API 분석 데이터
    *  - 나중에 실제 SoundNet 연동 예정
    */
    private MusicFeature getTrackFeatureFromRapidApi(String trackId, MusicSearchResponse musicInfo) {
        return MusicFeature.builder()
                .musicId(trackId)
                .albumId(musicInfo.getAlbumId())
                .trackName(musicInfo.getTrackName())
                .artistName(musicInfo.getArtistName())
                .popularity(0L)
                .energy(0L)
                .danceability(0L)
                .happiness(0L)
                .acousticness(0L)
                .instrumentalness(0L)
                .liveness(0L)
                .speechiness(0L)
                .build();
    }
}
