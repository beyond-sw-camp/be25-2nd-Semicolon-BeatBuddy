package com.beyond.beatbuddy.auth.service;

import com.beyond.beatbuddy.auth.dto.request.LoginRequestDto;
import com.beyond.beatbuddy.auth.dto.request.SignupRequestDto;
import com.beyond.beatbuddy.auth.dto.response.LoginResponseDto;
import com.beyond.beatbuddy.auth.entity.User;
import com.beyond.beatbuddy.auth.mapper.UserMapper;
import com.beyond.beatbuddy.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 (USER_002)
     */
    @Transactional
    public void signup(SignupRequestDto request) {
        if (userMapper.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .gender(request.getGender())
                .birthYear(request.getBirthYear())
                .build();

        userMapper.insertUser(user);
        log.info("[Auth] 회원가입 완료: {}", request.getEmail());
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailDuplicate(String email) {
        return userMapper.findByEmail(email) != null;
    }

    /**
     * 로그인 (USER_006) - JWT Access/Refresh 토큰 발급
     */
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken  = jwtUtil.generateAccessToken(user.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        userMapper.updateRefreshToken(user.getUserId(), refreshToken);
        log.info("[Auth] 로그인 완료: userId={}", user.getUserId());

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 토큰 재발급 (Refresh Token 검증)
     */
    @Transactional
    public LoginResponseDto reissue(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        Long userId = jwtUtil.getUserId(refreshToken);
        User user = userMapper.findById(userId);
        if (user == null || !refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token이 일치하지 않습니다.");
        }

        String newAccessToken  = jwtUtil.generateAccessToken(userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);
        userMapper.updateRefreshToken(userId, newRefreshToken);

        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(userId)
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 로그아웃 - Refresh Token 삭제
     */
    @Transactional
    public void logout(Long userId) {
        userMapper.deleteRefreshToken(userId);
        log.info("[Auth] 로그아웃: userId={}", userId);
    }
}
