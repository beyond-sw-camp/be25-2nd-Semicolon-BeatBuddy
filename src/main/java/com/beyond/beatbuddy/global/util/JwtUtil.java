package com.beyond.beatbuddy.global.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Date;

@Configuration
public class JwtUtil {
	private final Key key;

	// 현업에서는 이렇게 랜덤한 키 안 씀
	// 서버 꺼지고 켜질때마다 랜덤하게 한번씩 만들어짐
	private final long expiration = 1000L * 60 * 60; // 1시간

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username) // 토큰 안에 username 저장
				.setIssuedAt(new Date()) // 발급 시간 (지금)
				.setExpiration(new Date(System.currentTimeMillis()+expiration)) // 만료 시간
				.signWith(key) // 비밀키로 서명
				.compact(); // 문자열로 변환해서 반환
	}

	public String generateAccessToken(String username) {
		long expiration_30m = 1000L * 60 * 30;
		return Jwts.builder()
				.setSubject(username) // 토큰 안에 username 저장
				.setIssuedAt(new Date()) // 발급 시간 (지금)
				.setExpiration(new Date(System.currentTimeMillis() + expiration_30m)) // 만료 시간
				.signWith(key) // 비밀키로 서명
				.compact(); // 문자열로 변환해서 반환
	}

	public String generateRefreshToken(String username) {
		long expiration_7d = 1000L * 60 * 60 * 24 * 7;
		return Jwts.builder()
				.setSubject(username) // 토큰 안에 username 저장
				.setIssuedAt(new Date()) // 발급 시간 (지금)
				.setExpiration(new Date(System.currentTimeMillis() + expiration_7d)) // 만료 시간
				.signWith(key) // 비밀키로 서명
				.compact(); // 문자열로 변환해서 반환
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key) // 비밀키로 검증 준비
				.build()
				.parseClaimsJws(token) // 토큰 파싱
				.getBody() // 토큰 안의 데이터 꺼내기
				.getSubject(); // username 꺼내기
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		}
		catch (JwtException | IllegalArgumentException e) {
			// 전자: 만료, 위조, 서명 불일치
			// 후자: 토큰이 null이거나 빈 문자열("")
			return false;
		}
	}
}
