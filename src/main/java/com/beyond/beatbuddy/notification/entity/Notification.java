package com.beyond.beatbuddy.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long notificationId;
    private Long userId;
    private String content;
    private String type; // FRIEND_REQUEST | CHAT | SOCIAL | SYSTEM
    private boolean isRead;
    private LocalDateTime createdAt;
}
