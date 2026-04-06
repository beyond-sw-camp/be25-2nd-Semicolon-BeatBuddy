package com.beyond.beatbuddy.music.controller;

// 요청 받는 입구

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.global.security.UserPrincipal;
import com.beyond.beatbuddy.music.dto.request.MusicSearchRequest;
import com.beyond.beatbuddy.music.dto.request.TasteSaveRequest;
import com.beyond.beatbuddy.music.dto.response.MusicSearchResponse;
import com.beyond.beatbuddy.music.dto.response.TasteResponse;
import com.beyond.beatbuddy.music.dto.response.TasteSaveResponse;
import com.beyond.beatbuddy.music.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    /*
    * 음악 검색
    *  - 프론트에서 keyword를 받아 Spotify 검색 결과를 반환
    */

    @PostMapping("/search")
    @Operation(summary = "앨범, 아티스트, 곡 검색")
    public ResponseEntity<ApiResponse<List<MusicSearchResponse>>> searchMusic(
            @Valid @RequestBody MusicSearchRequest request
    ) {
        List<MusicSearchResponse> result = musicService.searchMusic(request);
        return ApiResponse.of(HttpStatus.OK, "음악 검색 성공", result);
    }

    /*
    * 최애곡 최초 저장
    *  - 프론트에서 선택한 10곡(trackId 목록)을 받아 저장
    */
    @PostMapping("/taste")
    @Operation(summary = "최애곡 10곡 저장")
    public ResponseEntity<ApiResponse<TasteSaveResponse>> saveTaste(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TasteSaveRequest request
    ) {
        Long userId = userPrincipal.getUserId();

        TasteSaveResponse result = musicService.saveTaste(userId, request);
        return ApiResponse.of(HttpStatus.OK, "최애곡 저장 성공", result);
    }

    /*
    * 저장된 최애곡 조회
    *  - 음악 탭 진입 시 저장된 취향 데이터가 있는지 확인하고 변환
    */
    @GetMapping("/taste")
    @Operation(summary = "최애곡 목록 조회")
    public ResponseEntity<ApiResponse<TasteResponse>> getTaste(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getUserId();

        TasteResponse result = musicService.getTaste(userId);
        return ApiResponse.of(HttpStatus.OK, "최애곡 조회 성공", result);
    }

    /*
    * 취향 수정
    *  - 기존 10곡을 전체 교체
    */
    @PutMapping("/taste")
    @Operation(summary = "최애곡 수정")
    public ResponseEntity<ApiResponse<TasteSaveResponse>> updateTaste(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TasteSaveRequest request
    ) {
        Long userId = userPrincipal.getUserId();

        TasteSaveResponse result = musicService.updateTaste(userId, request);
        return ApiResponse.of(HttpStatus.OK, "최애곡 수정 성공", result);
    }


}
