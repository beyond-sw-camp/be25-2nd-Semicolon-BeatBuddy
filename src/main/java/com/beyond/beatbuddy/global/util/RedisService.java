package com.beyond.beatbuddy.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final StringRedisTemplate stringRedisTemplate;
	/* refresh token 관련 */
	// refresh token 저장
	public void saveRefreshToken(Long userId, String refreshToken) {
		stringRedisTemplate.opsForValue()
				.set("refresh:" + userId, refreshToken, 7, TimeUnit.DAYS);
	}
	// refresh token 조회


	// refresh token 삭제


	/* access token 관련 */
	// access token 저장 - 블랙 리스트
	public void addBlackList(String accessToken, long expiration) {
		stringRedisTemplate.opsForValue()
				.set("blacklist:" + accessToken, "true", expiration, TimeUnit.MILLISECONDS);
	}
	// access token 확인 - 블랙리스트
	private boolean isBlacklisted(String accessToken) {
		return stringRedisTemplate.hasKey("blacklist:" + accessToken);
	}

	// email-verification 저장

	// email-verification 삭제

}
