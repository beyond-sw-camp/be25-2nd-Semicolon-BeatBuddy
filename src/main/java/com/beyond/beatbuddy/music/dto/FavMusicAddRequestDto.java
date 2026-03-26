package com.beyond.beatbuddy.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavMusicAddRequestDto {

    private String musicId;  // Spotify Track ID
}
