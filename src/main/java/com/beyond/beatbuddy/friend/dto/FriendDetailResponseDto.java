package com.beyond.beatbuddy.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendDetailResponseDto {
    private Long friendId;
    private String nickname;
    private String profileImageUrl;
    private String gender;
    private Integer birthYear;
    // 최애곡 목록 (music_id 기준)
    private List<String> favMusicIds;
}
