package com.beyond.beatbuddy.auth.service;

import com.beyond.beatbuddy.auth.dto.request.SignupRequest;
import com.beyond.beatbuddy.auth.dto.response.EmailSendResponse;
import com.beyond.beatbuddy.auth.dto.response.TokenResponse;
import com.beyond.beatbuddy.auth.mapper.UserMapper;
import com.beyond.beatbuddy.global.entity.User;
import com.beyond.beatbuddy.global.exception.ConflictException;
import com.beyond.beatbuddy.global.exception.UnauthorizedException;
import com.beyond.beatbuddy.global.util.FileStorageService;
import com.beyond.beatbuddy.global.util.JwtUtil;
import com.beyond.beatbuddy.global.util.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final FileStorageService fileStorageService;
	private final RedisService redisService;
	private final JwtUtil jwtUtil;
	private final EmailService emailService;

	public TokenResponse signUp(SignupRequest request, MultipartFile profileImage) {

//		// 1. 이메일 인증 확인
//		if (!redisService.isEmailVerified(request.getEmail())) {
//			throw new UnauthorizedException("이메일 인증이 필요합니다.");
//		}

		// 2. 이메일 중복 확인
		if (userMapper.existsByEmail(request.getEmail())) {
			throw new ConflictException("이미 사용 중인 이메일입니다.");
		}

		// 3. 프로필 사진 저장
		String profileImageUrl = fileStorageService.saveProfileImage(profileImage);

		// 4. 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		// 5. 유저 저장
		User user = User.builder()
				.email(request.getEmail())
				.password(encodedPassword)
				.nickname(request.getNickname())
				.gender(request.getGender())
				.birthYear(request.getBirthYear())
				.profileImageUrl(profileImageUrl)
				.provider("LOCAL")
				.status("ACTIVE")
				.build();

		userMapper.save(user);

		// 6. verified 삭제
		redisService.deleteVerified(request.getEmail());

		// 7. 토큰 발급
		String accessToken = jwtUtil.generateAccessToken(
				user.getUserId(), user.getEmail(), user.getNickname());
		String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

		// 8. Refresh Token Redis 저장
		redisService.saveRefreshToken(user.getUserId(), refreshToken);

		return TokenResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.userId(user.getUserId())
				.email(user.getEmail())
				.nickname(user.getNickname())
				.build();
	}

	// 인증코드 발송
	public EmailSendResponse sendVerificationCode(String email) {
		// 1. 이미 가입된 이메일인지 확인
		if (userMapper.existsByEmail(email)) {
			throw new ConflictException("이미 사용 중인 이메일입니다.");
		}

		// 2. 6자리 랜덤 코드 생성
		String code = String.format("%06d", new Random().nextInt(1000000));

		// 3. 기존 인증 데이터 초기화 (재발송 경우)
		redisService.resetVerificationCode(email);

		// 4. Redis에 코드 저장
		redisService.saveVerificationCode(email, code);

		// 5. 이메일 발송
		emailService.sendVerificationEmail(email, code);

		return EmailSendResponse.builder()
				.attempts(0)
				.maxAttempts(5)
				.build();
	}
}
