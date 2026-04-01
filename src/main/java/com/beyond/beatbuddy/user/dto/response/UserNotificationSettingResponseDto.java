package com.beyond.beatbuddy.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNotificationSettingResponseDto {
    private final int code = 200;
    private Boolean allowPushChat;
    private Boolean allowPushSocial;
}
