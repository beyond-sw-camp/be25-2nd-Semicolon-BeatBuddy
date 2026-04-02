package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.global.exception.BadRequestException;
import com.beyond.beatbuddy.global.exception.NotFoundException;
import com.beyond.beatbuddy.music.dto.request.FavMusicRequest;
import com.beyond.beatbuddy.music.dto.request.MusicSaveRequest;
import com.beyond.beatbuddy.music.dto.response.FavMusicResponse;
import com.beyond.beatbuddy.music.dto.response.MusicSearchResponse;
import com.beyond.beatbuddy.music.dto.response.TasteVectorResponse;
import com.beyond.beatbuddy.music.entity.Album;
import com.beyond.beatbuddy.music.entity.MusicFeature;
import com.beyond.beatbuddy.music.entity.UserFavMusic;
import com.beyond.beatbuddy.music.mapper.AlbumMapper;
import com.beyond.beatbuddy.music.mapper.MusicFeatureMapper;
import com.beyond.beatbuddy.music.mapper.UserFavMusicMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicService {

    private static final int MAX_FAV_MUSIC = 10;

    private final SpotifyApi spotifyApi;
    private final AlbumMapper albumMapper;
    private final MusicFeatureMapper musicFeatureMapper;
    private final UserFavMusicMapper userFavMusicMapper;
    private final ObjectMapper objectMapper;

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    // ───────────────────────────────────────────────
    // Spotify 액세스 토큰 갱신 (Client Credentials Flow)
    // ───────────────────────────────────────────────
    private void refreshSpotifyToken() {
        try {
            ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
            ClientCredentials credentials = request.execute();
            spotifyApi.setAccessToken(credentials.getAccessToken());
        } catch (Exception e) {
            throw new RuntimeException("Spotify 인증 토큰 갱신 실패: " + e.getMessage(), e);
        }
    }

    // ───────────────────────────────────────────────
    // MUSIC_002: DB에서 키워드로 음악 검색
    // ───────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<MusicSearchResponse> searchMusic(String keyword) {
        return musicFeatureMapper.searchByKeyword(keyword);
    }

    // ───────────────────────────────────────────────
    // Spotify API에서 트랙 검색 (저장 전 곡 탐색용)
    // ───────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<MusicSearchResponse> searchFromSpotify(String query) {
        refreshSpotifyToken();
        try {
            var searchResult = spotifyApi.searchTracks(query).limit(20).build().execute();
            return Arrays.stream(searchResult.getItems())
                    .map(track -> MusicSearchResponse.builder()
                            .musicId(track.getId())
                            .trackName(track.getName())
                            .artistName(track.getArtists()[0].getName())
                            .albumName(track.getAlbum().getName())
                            .albumCoverUrl(track.getAlbum().getImages().length > 0
                                    ? track.getAlbum().getImages()[0].getUrl()
                                    : null)
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Spotify 검색 실패: " + e.getMessage(), e);
        }
    }

    // ───────────────────────────────────────────────
    // MUSIC_001: Spotify + RapidAPI 연동 → DB 저장
    // ───────────────────────────────────────────────
    @Transactional
    public void saveMusic(MusicSaveRequest request) {
        String trackId = request.getSpotifyTrackId();

        // 이미 저장된 곡이면 스킵
        if (musicFeatureMapper.findById(trackId) != null) {
            return;
        }

        // 1. Spotify에서 트랙 정보 조회
        refreshSpotifyToken();
        Track track;
        try {
            track = spotifyApi.getTrack(trackId).build().execute();
        } catch (Exception e) {
            throw new NotFoundException("Spotify에서 트랙을 찾을 수 없습니다: " + trackId);
        }

        String albumId = track.getAlbum().getId();
        String albumName = track.getAlbum().getName();
        String albumCoverUrl = track.getAlbum().getImages().length > 0
                ? track.getAlbum().getImages()[0].getUrl()
                : null;
        String trackName = track.getName();
        String artistName = track.getArtists()[0].getName();
        long popularity = track.getPopularity();

        // 2. 앨범 저장 (ON DUPLICATE KEY UPDATE - covers/name 갱신)
        albumMapper.insertAlbum(Album.builder()
                .albumId(albumId)
                .albumName(albumName)
                .albumCoverUrl(albumCoverUrl)
                .build());

        // 3. RapidAPI에서 오디오 분석 데이터 조회
        JsonNode analysisNode = fetchRapidApiAnalysis(trackName, artistName);

        // 4. RapidAPI 응답값: 0.0~1.0 float → ×100 → Long (DB BIGINT 맵핑)
        MusicFeature feature = MusicFeature.builder()
                .musicId(trackId)
                .albumId(albumId)
                .trackName(trackName)
                .artistName(artistName)
                .popularity(popularity)
                .energy(toLong(analysisNode, "energy"))
                .danceability(toLong(analysisNode, "danceability"))
                .happiness(toLong(analysisNode, "happiness"))
                .acousticness(toLong(analysisNode, "acousticness"))
                .instrumentalness(toLong(analysisNode, "instrumentalness"))
                .liveness(toLong(analysisNode, "liveness"))
                .speechiness(toLong(analysisNode, "speechiness"))
                .build();

        // 5. music_features 저장 (중복이면 INSERT IGNORE로 그냥 넘어감)
        musicFeatureMapper.insertMusicFeature(feature);
    }

    // ───────────────────────────────────────────────
    // RapidAPI Track Analysis 호출
    // ───────────────────────────────────────────────
    private JsonNode fetchRapidApiAnalysis(String trackName, String artistName) {
        try {
            String encodedSong = URLEncoder.encode(trackName, StandardCharsets.UTF_8);
            String encodedArtist = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
            String url = "https://track-analysis.p.rapidapi.com/pktx/analysis"
                    + "?song=" + encodedSong
                    + "&artist=" + encodedArtist;

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("x-rapidapi-key", rapidApiKey)
                    .header("x-rapidapi-host", "track-analysis.p.rapidapi.com")
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("RapidAPI 분석 요청 실패: " + e.getMessage(), e);
        }
    }

    // 0.0~1.0 float → 0~100 Long 변환 (없으면 0)
    private Long toLong(JsonNode node, String field) {
        if (node == null || node.get(field) == null || node.get(field).isNull())
            return 0L;
        return Math.round(node.get(field).asDouble() * 100);
    }

    // ───────────────────────────────────────────────
    // MUSIC_004: 최애곡 추가 (10개 제한)
    // ───────────────────────────────────────────────
    @Transactional
    public void addFavorite(Long userId, FavMusicRequest request) {
        int count = userFavMusicMapper.countFavMusic(userId);
        if (count >= MAX_FAV_MUSIC) {
            throw new BadRequestException("최애곡은 최대 " + MAX_FAV_MUSIC + "개까지 등록할 수 있습니다.");
        }

        if (musicFeatureMapper.findById(request.getMusicId()) == null) {
            throw new NotFoundException("해당 음악이 존재하지 않습니다. 먼저 /music/save로 저장해주세요.");
        }

        userFavMusicMapper.insertFavMusic(UserFavMusic.builder()
                .userId(userId)
                .musicId(request.getMusicId())
                .build());
    }

    // ───────────────────────────────────────────────
    // MUSIC_005: 내 최애곡 목록 조회
    // ───────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<FavMusicResponse> getFavorites(Long userId) {
        return userFavMusicMapper.findFavMusicByUserId(userId);
    }

    // ───────────────────────────────────────────────
    // MUSIC_003: 최애곡 삭제
    // ───────────────────────────────────────────────
    @Transactional
    public void removeFavorite(Long userId, String musicId) {
        userFavMusicMapper.deleteFavMusic(userId, musicId);
    }

    // ───────────────────────────────────────────────
    // 취향 분석: AVG+STDDEV → TasteVectorResponse
    // ───────────────────────────────────────────────
    @Transactional(readOnly = true)
    public TasteVectorResponse analyzeTaste(Long userId) {
        TasteVectorResponse result = userFavMusicMapper.analyzeTasteVector(userId);
        if (result == null) {
            throw new NotFoundException("취향 분석을 위한 최애곡 데이터가 없습니다.");
        }
        return result;
    }
}
