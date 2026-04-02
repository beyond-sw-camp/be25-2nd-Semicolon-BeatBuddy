package com.beyond.beatbuddy.music.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavMusicResponse {
    private String musicId;
    private String trackName;
    private String artistName;
    private String albumName;
    private String albumCoverUrl;
}
