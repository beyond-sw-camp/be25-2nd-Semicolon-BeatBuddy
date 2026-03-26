package com.beyond.beatbuddy.global.security;

import com.beyond.beatbuddy.auth.entity.User;
import com.beyond.beatbuddy.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.findById(Long.parseLong(userId));
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(user.getUserId()))
                .password(user.getPassword() != null ? user.getPassword() : "")
                .authorities(Collections.emptyList())
                .build();
    }
}
