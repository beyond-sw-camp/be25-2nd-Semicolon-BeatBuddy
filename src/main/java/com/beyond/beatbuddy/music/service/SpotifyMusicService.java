package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.music.dto.MusicSearchItemDto;
import com.beyond.beatbuddy.music.entity.Album;
import com.beyond.beatbuddy.music.entity.MusicFeature;
import com.beyond.beatbuddy.music.external.SpotifyApiClient;
import com.beyond.beatbuddy.music.mapper.AlbumMapper;
import com.beyond.beatbuddy.music.mapper.MusicFeatureMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyMusicService {

    private final SpotifyApiClient spotifyApiClient;
    private final AlbumMapper albumMapper;
    private final MusicFeatureMapper musicFeatureMapper;

    /**
     * Spotify 음악 검색
     */
    @SuppressWarnings("unchecked")
    public List<MusicSearchItemDto> searchTracks(String query, int limit) {
        Map<?, ?> body = spotifyApiClient.searchTracks(query, limit);
        Map<?, ?> tracks = (Map<?, ?>) body.get("tracks");
        List<Map<?, ?>> items = (List<Map<?, ?>>) tracks.get("items");

        List<MusicSearchItemDto> result = new ArrayList<>();
        for (Map<?, ?> item : items) {
            Map<?, ?> album = (Map<?, ?>) item.get("album");
            List<Map<?, ?>> albumImages = (List<Map<?, ?>>) album.get("images");
            String albumCoverUrl = albumImages.isEmpty() ? null : (String) albumImages.get(0).get("url");

            List<Map<?, ?>> artists = (List<Map<?, ?>>) item.get("artists");
            String artistName = artists.stream()
                    .map(a -> (String) a.get("name"))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            result.add(MusicSearchItemDto.builder()
                    .musicId((String) item.get("id"))
                    .trackName((String) item.get("name"))
                    .artistName(artistName)
                    .albumId((String) album.get("id"))
                    .albumName((String) album.get("name"))
                    .albumCoverUrl(albumCoverUrl)
                    .popularity(item.get("popularity") != null ? ((Number) item.get("popularity")).longValue() : 0L)
                    .build());
        }
        return result;
    }

    /**
     * 앨범 정보 DB 저장 (동기화)
     */
    @SuppressWarnings("unchecked")
    public void syncAlbum(String albumId) {
        Map<?, ?> albumData = spotifyApiClient.getAlbum(albumId);
        List<Map<?, ?>> images = (List<Map<?, ?>>) albumData.get("images");
        String coverUrl = (images != null && !images.isEmpty()) ? (String) images.get(0).get("url") : null;

        Album album = Album.builder()
                .albumId((String) albumData.get("id"))
                .albumName((String) albumData.get("name"))
                .albumCoverUrl(coverUrl)
                .build();

        albumMapper.insertAlbum(album);
        log.info("[Spotify] 앨범 동기화 완료: {}", albumId);
    }

    /**
     * 트랙 + 음악 특성 DB 저장 (동기화)
     * - 앨범도 함께 동기화
     */
    @SuppressWarnings("unchecked")
    public MusicFeature syncMusicFeature(String trackId) {
        Map<?, ?> trackData = spotifyApiClient.getTrack(trackId);
        Map<?, ?> albumData = (Map<?, ?>) trackData.get("album");
        String albumId = (String) albumData.get("id");

        // 1) 앨범 동기화
        syncAlbum(albumId);

        // 2) 아티스트명 추출
        List<Map<?, ?>> artists = (List<Map<?, ?>>) trackData.get("artists");
        String artistName = artists.stream()
                .map(a -> (String) a.get("name"))
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        // 3) Audio Features 조회
        Map<?, ?> features = spotifyApiClient.getAudioFeature(trackId);

        // Spotify 0.0~1.0 → 0~100 변환, popularity는 그대로
        MusicFeature musicFeature = MusicFeature.builder()
                .musicId(trackId)
                .albumId(albumId)
                .trackName((String) trackData.get("name"))
                .artistName(artistName)
                .popularity(trackData.get("popularity") != null ? ((Number) trackData.get("popularity")).longValue() : 0L)
                .energy(toScale100(features.get("energy")))
                .danceability(toScale100(features.get("danceability")))
                .happiness(toScale100(features.get("valence")))
                .acousticness(toScale100(features.get("acousticness")))
                .instrumentalness(toScale100(features.get("instrumentalness")))
                .liveness(toScale100(features.get("liveness")))
                .speechiness(toScale100(features.get("speechiness")))
                .build();

        musicFeatureMapper.insertMusicFeature(musicFeature);
        log.info("[Spotify] 음악 데이터 동기화 완료: {}", trackId);
        return musicFeature;
    }

    /** 0.0~1.0 값을 0~100 Long으로 변환 */
    private Long toScale100(Object value) {
        if (value == null) return null;
        return Math.round(((Number) value).doubleValue() * 100);
    }
}
