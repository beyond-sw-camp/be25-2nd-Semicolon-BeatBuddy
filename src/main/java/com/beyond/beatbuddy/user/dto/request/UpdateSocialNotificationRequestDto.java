package com.beyond.beatbuddy.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSocialNotificationRequestDto {
    @NotNull(message = "allowPushSocial is required.")
    private Boolean allowPushSocial;
}
