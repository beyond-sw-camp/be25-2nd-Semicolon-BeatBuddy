package com.beyond.beatbuddy.music.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MusicSearchItemDto {

    private String musicId;       // Spotify Track ID
    private String trackName;
    private String artistName;
    private String albumId;
    private String albumName;
    private String albumCoverUrl;
    private Long popularity;
}
