package com.beyond.beatbuddy.music.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    private String albumId;       // Spotify Album ID (PK)
    private String albumName;
    private String albumCoverUrl;
}
