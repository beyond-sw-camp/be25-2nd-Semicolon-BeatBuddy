package com.beyond.beatbuddy.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private Integer birthYear;
    private String profileImageUrl;
    private String status;
}