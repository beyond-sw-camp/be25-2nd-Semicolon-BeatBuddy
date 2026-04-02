package com.beyond.beatbuddy.music.controller;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.global.security.UserPrincipal;
import com.beyond.beatbuddy.music.dto.request.FavMusicRequest;
import com.beyond.beatbuddy.music.dto.request.MusicSaveRequest;
import com.beyond.beatbuddy.music.dto.response.FavMusicResponse;
import com.beyond.beatbuddy.music.dto.response.MusicSearchResponse;
import com.beyond.beatbuddy.music.dto.response.TasteVectorResponse;
import com.beyond.beatbuddy.music.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
@Tag(name = "Music", description = "음악 검색·저장·최애곡 관리 API")
public class MusicController {

    private final MusicService musicService;

    /** MUSIC_002: DB 키워드 검색 (곡명 / 아티스트 / 앨범명) */
    @GetMapping("/search")
    @Operation(summary = "음악 검색 (DB)", description = "곡명, 아티스트, 앨범명으로 DB 내 음악을 검색합니다.")
    public ResponseEntity<ApiResponse<List<MusicSearchResponse>>> searchMusic(
            @RequestParam String query) {
        return ApiResponse.of(HttpStatus.OK, "음악 검색 성공", musicService.searchMusic(query));
    }

    /** Spotify 실시간 검색 (저장 전 곡 탐색용) */
    @GetMapping("/search/spotify")
    @Operation(summary = "음악 검색 (Spotify)", description = "Spotify에서 실시간으로 트랙을 검색합니다. 저장하려면 /save를 호출하세요.")
    public ResponseEntity<ApiResponse<List<MusicSearchResponse>>> searchFromSpotify(
            @RequestParam String query) {
        return ApiResponse.of(HttpStatus.OK, "Spotify 검색 성공", musicService.searchFromSpotify(query));
    }

    /** MUSIC_001: Spotify Track ID로 곡 분석 데이터 저장 */
    @PostMapping("/save")
    @Operation(summary = "음악 저장", description = "Spotify Track ID로 앨범 정보와 RapidAPI 오디오 분석 데이터를 DB에 저장합니다.")
    public ResponseEntity<ApiResponse<Void>> saveMusic(@RequestBody MusicSaveRequest request) {
        musicService.saveMusic(request);
        return ApiResponse.of(HttpStatus.OK, "음악 저장 완료", null);
    }

    /** MUSIC_004: 최애곡 추가 (최대 10개) */
    @PostMapping("/favorite")
    @Operation(summary = "최애곡 추가", description = "음악을 내 최애곡에 추가합니다. 최대 10개까지 등록 가능합니다.")
    public ResponseEntity<ApiResponse<Void>> addFavorite(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody FavMusicRequest request) {
        musicService.addFavorite(principal.getUserId(), request);
        return ApiResponse.of(HttpStatus.OK, "최애곡 추가 완료", null);
    }

    /** MUSIC_005: 내 최애곡 목록 조회 */
    @GetMapping("/favorite")
    @Operation(summary = "내 최애곡 조회", description = "로그인한 사용자의 최애곡 목록을 최신순으로 반환합니다.")
    public ResponseEntity<ApiResponse<List<FavMusicResponse>>> getFavorites(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.of(HttpStatus.OK, "최애곡 조회 성공", musicService.getFavorites(principal.getUserId()));
    }

    /** MUSIC_003: 최애곡 삭제 */
    @DeleteMapping("/favorite/{musicId}")
    @Operation(summary = "최애곡 삭제", description = "내 최애곡에서 특정 음악을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String musicId) {
        musicService.removeFavorite(principal.getUserId(), musicId);
        return ApiResponse.of(HttpStatus.OK, "최애곡 삭제 완료", null);
    }

    /** 취향 분석: AVG + STDDEV → 16차원 벡터 반환 */
    @PostMapping("/taste-analyze")
    @Operation(summary = "취향 분석", description = "사용자의 최애곡을 기반으로 AVG+STDDEV 16차원 취향 벡터를 계산합니다.")
    public ResponseEntity<ApiResponse<TasteVectorResponse>> analyzeTaste(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.of(HttpStatus.OK, "취향 분석 완료", musicService.analyzeTaste(principal.getUserId()));
    }
}
