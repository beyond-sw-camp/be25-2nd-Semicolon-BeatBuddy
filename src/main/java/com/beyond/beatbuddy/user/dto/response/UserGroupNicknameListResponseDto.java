package com.beyond.beatbuddy.user.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class UserGroupNicknameListResponseDto {
    private final int code;
    private final List<UserGroupNicknameItemResponseDto> groups;

    public UserGroupNicknameListResponseDto(List<UserGroupNicknameItemResponseDto> groups) {
        this.code = 200;
        this.groups = groups;
    }
}
