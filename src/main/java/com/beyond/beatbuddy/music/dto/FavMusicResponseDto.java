package com.beyond.beatbuddy.music.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FavMusicResponseDto {

    private String musicId;
    private String trackName;
    private String artistName;
    private String albumName;
    private String albumCoverUrl;
    private LocalDateTime createdAt;
}
