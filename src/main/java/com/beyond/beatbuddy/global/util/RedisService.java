package com.beyond.beatbuddy.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final StringRedisTemplate stringRedisTemplate;

	
}
