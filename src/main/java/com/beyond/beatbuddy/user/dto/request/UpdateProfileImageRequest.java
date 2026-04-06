package com.beyond.beatbuddy.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileImageRequest {

    @NotBlank(message = "이미지를 입력해주세요.")
    private String profileImageUrl;
}