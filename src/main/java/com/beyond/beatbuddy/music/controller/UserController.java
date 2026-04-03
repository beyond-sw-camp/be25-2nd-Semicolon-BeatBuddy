package com.beyond.beatbuddy.music.controller;

import com.beyond.beatbuddy.music.entity.Users;
import com.beyond.beatbuddy.music.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<Users> getAll() {
        return userService.getAll();
    }
}
