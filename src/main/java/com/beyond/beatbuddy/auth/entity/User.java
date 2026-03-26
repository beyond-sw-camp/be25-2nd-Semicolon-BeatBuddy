package com.beyond.beatbuddy.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long userId;
    private String email;
    private String password;
    private String kakaoId;
    private String nickname;
    private String gender;
    private Long birthYear;
    private String profileImageUrl;
    private boolean isTutorialViewed;
    private boolean isTasteAnalyzed;
    private String status;           // ACTIVE / DELETED
    private boolean allowPushChat;
    private boolean allowPushSocial;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
