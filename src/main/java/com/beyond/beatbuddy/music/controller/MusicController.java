package com.beyond.beatbuddy.music.controller;

import com.beyond.beatbuddy.global.dto.ApiResponse;
import com.beyond.beatbuddy.music.dto.request.SaveTasteRequest;
import com.beyond.beatbuddy.music.dto.response.TrackSearchResponse;
import com.beyond.beatbuddy.music.service.MusicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/music")
public class MusicController {

	private final MusicService musicService;

	// 사용자가 본인의 최애 음악을 서치할 때 호출
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<TrackSearchResponse>>> searchTracks(
			@RequestParam @NotBlank(message = "검색어를 입력해주세요") String keyword) {
		List<TrackSearchResponse> response = musicService.searchTracks(keyword);
		return ApiResponse.of(HttpStatus.OK, "음악 검색이 완료됐습니다.", response);
	}

	// 사용자가 본인의 최애 음악 10개를 고르고 나서 호출
	@PostMapping("/taste")
	public ResponseEntity<ApiResponse<Void>> saveTaste(
			@RequestBody @Valid SaveTasteRequest request) {
		musicService.saveTaste(request);
		return ApiResponse.of(HttpStatus.OK, "취향이 저장됐습니다.", null);
	}

	// config에서 열어놨음 로그인 안해도 접속 가능하도록,,,
	// 스포티파이 곡 아이디로 곡 특성 가져오기
	@GetMapping("/justfortest")
	public ResponseEntity<ApiResponse<Object>> searchTest (
			@RequestParam String spotifyId) {
		Object data = musicService.searchTest(spotifyId);
		return ApiResponse.of(HttpStatus.OK, "테스트 - 음악 특성 가져오기 성공", data);
	}

	// 그렇다면 무엇이 남았을까...!!!

	// 취향 조회 (GET /taste) - 음악 탭 진입 시 호출
	// 음악 탭 진입할 때 호출해서 두 가지를 판단해야 함
	// 취향이 저장된 사람이야? → is_taste_analyzed 로 판단
	// 저장된 10곡이 뭐야? → 프로필 모드에서 보여줄 곡 목록
	// users 테이블에서 is_taste_analyzed를 확인하고
	// user_fav_music JOIN해서 곡 목록을 가져와야겠죠?

	// 취향 수정 (PUT /taste)
	// 코드를 잘 읽어보면 쉬울겁니다

	// 저도 지금까지 구현한거 전체 과정을 해봤는데, 뭐 엣지 케이스가 있을 수 있어서 확인해보셔야해요
	// 그것까지는 제가 하기는 어려울거 같네요

	// mariadb에서 user_profiles를 조회하면 xB >A33[Bu8ßA335B» 이렇게 염병 나는 이유
	// [0.5, 0.3, ...] 이런 텍스트를 VEC_FromText()가 바이너리로 변환해서 저장
	// 실제로 잘 들어갔는지 확인하려면 아까 말한 것처럼 SQL로 봐야 함.
	// SELECT user_id, VEC_ToText(taste_vector)
	// FROM user_profiles
	// WHERE user_id = 11;

	// 음악 분석 API가 중간 요금제라 1초에 1개씩밖에 요청을 못 받음 - 그래서 제가 임의로 1초씩 딜레이되게 막아놔서 느림
	// 딜레이 안 걸면 에러 남
	// 그냥 참거나 요금제를 바꾸거나...

	// swagger에서 회원가입하는 법: http://localhost:8088/swagger-ui/index.html#/Auth%20APIs (dev로 돌렸으면)
	// redis랑 mariadb다 켜셔야 하고
	// 여기에서 auth/email/send, auth/email/verify, auth/signup순으로 하시면 됩니다
	// 그리고 responsebody에 accesstoken 나오면
	// 음악 api 테스트는 오른쪽에 좌물쇠 보이시죠? 거기다가 accesstoken 복붙하세요
}
