package com.beyond.beatbuddy.music.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyApiClient {

    private final SpotifyProperties spotifyProperties;
    private final SpotifyTokenManager tokenManager;

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl(spotifyProperties.getApiBaseUrl())
                .defaultHeader("Authorization", "Bearer " + tokenManager.getAccessToken())
                .build();
    }

    /**
     * 음악 검색 (트랙 기준)
     * @param query 검색어
     * @param limit 최대 결과 수 (기본 10)
     */
    public Map<?, ?> searchTracks(String query, int limit) {
        log.info("[Spotify] 음악 검색: query={}, limit={}", query, limit);
        return webClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("type", "track")
                        .queryParam("limit", limit)
                        .queryParam("market", "KR")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * 트랙 단건 조회
     */
    public Map<?, ?> getTrack(String trackId) {
        log.info("[Spotify] 트랙 조회: trackId={}", trackId);
        return webClient()
                .get()
                .uri("/tracks/{id}", trackId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * 앨범 단건 조회
     */
    public Map<?, ?> getAlbum(String albumId) {
        log.info("[Spotify] 앨범 조회: albumId={}", albumId);
        return webClient()
                .get()
                .uri("/albums/{id}", albumId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * 트랙 여러 개의 Audio Features 조회 (쉼표 구분 ID)
     */
    public Map<?, ?> getAudioFeatures(List<String> trackIds) {
        String ids = String.join(",", trackIds);
        log.info("[Spotify] Audio Features 조회: ids={}", ids);
        return webClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/audio-features")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * 단일 트랙 Audio Features 조회
     */
    public Map<?, ?> getAudioFeature(String trackId) {
        log.info("[Spotify] Audio Feature 조회: trackId={}", trackId);
        return webClient()
                .get()
                .uri("/audio-features/{id}", trackId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
