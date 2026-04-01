package com.beyond.beatbuddy.user.dto.response;

import lombok.Getter;

@Getter
public class UserNotificationUpdateResponseDto {
    private final int code;
    private final String message;

    public UserNotificationUpdateResponseDto(String message) {
        this.code = 200;
        this.message = message;
    }
}
