package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.music.entity.Users;
import com.beyond.beatbuddy.music.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public List<Users> getAll() {
        return userMapper.findAll();
    }
}
