package com.beyond.beatbuddy.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGroupNicknameItemResponseDto {
    private Long groupId;
    private String groupName;
    private String groupImageUrl;
    private String groupNickname;
}
