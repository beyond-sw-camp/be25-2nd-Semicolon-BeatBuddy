package com.beyond.beatbuddy.auth.service;

import com.beyond.beatbuddy.auth.dto.request.SignupRequest;
import com.beyond.beatbuddy.auth.dto.response.LoginResponse;
import com.beyond.beatbuddy.auth.mapper.UserMapper;
import com.beyond.beatbuddy.global.entity.User;
import com.beyond.beatbuddy.global.util.FileStorageService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final FileStorageService fileStorageService;

	// 회원가입
	public void signUp(SignupRequest request, MultipartFile profileImage) {

		// 1. 이메일 중복 확인
		if (userMapper.existsByEmail(request.getEmail())) {
			throw new RuntimeException("이미 사용 중인 이메일입니다.");
		}

		// 2. 프로필 사진 저장 (없으면 기본 이미지)
		String profileImageUrl = fileStorageService.saveProfileImage(profileImage);

		// 3. 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		// 4. 유저 저장
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

		user = userMapper.findByEmail(request.getEmail());
	}

	// 로그인
	public LoginResponse login(
			String email,
			String password) {
		LoginResponse loginResponse = new LoginResponse();
		return loginResponse;
	}
}
