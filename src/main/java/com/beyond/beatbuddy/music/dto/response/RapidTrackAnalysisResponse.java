package com.beyond.beatbuddy.music.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RapidTrackAnalysisResponse {

    private Integer popularity;
    private Integer energy;
    private Integer danceability;
    private Integer happiness;
    private Integer acousticness;
    private Integer instrumentalness;
    private Integer liveness;
    private Integer speechiness;
}
