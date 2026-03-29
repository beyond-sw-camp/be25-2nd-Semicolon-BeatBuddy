package com.beyond.beatbuddy.music.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyTokenManager {

    private final SpotifyProperties spotifyProperties;

    private String cachedToken;
    private Instant tokenExpiry;

    /**
     * Spotify Client Credentials Flow로 Access Token 발급 및 캐싱
     */
    public synchronized String getAccessToken() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }

        log.info("[Spotify] Access Token 발급 요청");

        Map<?, ?> response = WebClient.create()
                .post()
                .uri(spotifyProperties.getTokenUrl())
                .headers(headers -> headers.setBasicAuth(
                        spotifyProperties.getClientId(),
                        spotifyProperties.getClientSecret()
                ))
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Spotify Access Token 발급 실패");
        }

        cachedToken = (String) response.get("access_token");
        int expiresIn = (Integer) response.get("expires_in"); // 보통 3600초
        tokenExpiry = Instant.now().plusSeconds(expiresIn - 60); // 60초 여유

        log.info("[Spotify] Access Token 발급 완료 (만료까지 {}초)", expiresIn - 60);
        return cachedToken;
    }
}
