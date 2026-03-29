package com.beyond.beatbuddy.music.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicFeature {

    private String musicId;         // Spotify Track ID (PK)
    private String albumId;         // FK → albums
    private String trackName;
    private String artistName;      // 복수 아티스트 시 콤마 구분

    // 음악 특성 (0~100 정수)
    private Long popularity;
    private Long energy;
    private Long danceability;
    private Long happiness;         // Spotify: valence
    private Long acousticness;
    private Long instrumentalness;
    private Long liveness;
    private Long speechiness;
}
