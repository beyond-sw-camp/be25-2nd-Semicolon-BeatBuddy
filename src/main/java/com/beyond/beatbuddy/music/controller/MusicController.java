package com.beyond.beatbuddy.music.controller;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.music.dto.FavMusicAddRequestDto;
import com.beyond.beatbuddy.music.dto.FavMusicResponseDto;
import com.beyond.beatbuddy.music.dto.MusicSearchItemDto;
import com.beyond.beatbuddy.music.service.SpotifyMusicService;
import com.beyond.beatbuddy.music.service.UserFavMusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {

    private final SpotifyMusicService spotifyMusicService;
    private final UserFavMusicService userFavMusicService;

    /**
     * 음악 검색 (MUSIC_002)
     * GET /api/music/search?q={query}&limit={limit}
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MusicSearchItemDto>>> searchMusic(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit) {
        List<MusicSearchItemDto> result = spotifyMusicService.searchTracks(q, limit);
        return ApiResponse.of(HttpStatus.OK, "음악 검색 성공", result);
    }

    /**
     * 앨범 동기화 (MUSIC_001 - 내부용)
     * POST /api/music/sync/album/{albumId}
     */
    @PostMapping("/sync/album/{albumId}")
    public ResponseEntity<ApiResponse<Void>> syncAlbum(@PathVariable String albumId) {
        spotifyMusicService.syncAlbum(albumId);
        return ApiResponse.of(HttpStatus.OK, "앨범 동기화 완료", null);
    }

    /**
     * 트랙 동기화 (MUSIC_001 - 내부용)
     * POST /api/music/sync/track/{trackId}
     */
    @PostMapping("/sync/track/{trackId}")
    public ResponseEntity<ApiResponse<Void>> syncTrack(@PathVariable String trackId) {
        spotifyMusicService.syncMusicFeature(trackId);
        return ApiResponse.of(HttpStatus.OK, "트랙 동기화 완료", null);
    }

    /**
     * 최애곡 추가 (MUSIC_003)
     * POST /api/music/fav
     */
    @PostMapping("/fav")
    public ResponseEntity<ApiResponse<Void>> addFav(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FavMusicAddRequestDto request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userFavMusicService.addFav(userId, request);
        return ApiResponse.of(HttpStatus.CREATED, "최애곡 추가 완료", null);
    }

    /**
     * 최애곡 삭제 (MUSIC_003)
     * DELETE /api/music/fav/{musicId}
     */
    @DeleteMapping("/fav/{musicId}")
    public ResponseEntity<ApiResponse<Void>> deleteFav(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String musicId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userFavMusicService.deleteFav(userId, musicId);
        return ApiResponse.of(HttpStatus.OK, "최애곡 삭제 완료", null);
    }

    /**
     * 최애곡 목록 조회 (MUSIC_005)
     * GET /api/music/fav
     */
    @GetMapping("/fav")
    public ResponseEntity<ApiResponse<List<FavMusicResponseDto>>> getFavList(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        List<FavMusicResponseDto> list = userFavMusicService.getFavList(userId);
        return ApiResponse.of(HttpStatus.OK, "최애곡 목록 조회 성공", list);
    }

    /**
     * 저장 개수 확인 (MUSIC_004)
     * GET /api/music/fav/count
     */
    @GetMapping("/fav/count")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getFavCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        int count = userFavMusicService.getFavCount(userId);
        return ApiResponse.of(HttpStatus.OK, "저장 개수 조회 성공", Map.of("count", count));
    }

    /**
     * 10곡 확정 및 취향 분석 (MUSIC_004 확정 + 취향 분석)
     * POST /api/music/fav/confirm
     */
    @PostMapping("/fav/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmFav(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userFavMusicService.confirmAndAnalyze(userId);
        return ApiResponse.of(HttpStatus.OK, "취향 분석 완료", null);
    }
}
