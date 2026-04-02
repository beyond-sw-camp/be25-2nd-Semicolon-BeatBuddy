package com.beyond.beatbuddy.music.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicFeature {
    private String musicId;
    private String albumId;
    private String trackName;
    private String artistName;
    private Long popularity;
    private Long energy;
    private Long danceability;
    private Long happiness;
    private Long acousticness;
    private Long instrumentalness;
    private Long liveness;
    private Long speechiness;
}
