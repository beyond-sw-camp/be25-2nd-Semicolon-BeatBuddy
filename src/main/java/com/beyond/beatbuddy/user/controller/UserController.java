package com.beyond.beatbuddy.user.controller;

import com.beyond.beatbuddy.user.dto.UserProfileResponseDto;
import com.beyond.beatbuddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponseDto getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getMyProfile(userDetails.getUsername());
    }
}