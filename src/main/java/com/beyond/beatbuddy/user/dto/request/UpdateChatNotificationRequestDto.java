package com.beyond.beatbuddy.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateChatNotificationRequestDto {
    @NotNull(message = "allowPushChat is required.")
    private Boolean allowPushChat;
}
