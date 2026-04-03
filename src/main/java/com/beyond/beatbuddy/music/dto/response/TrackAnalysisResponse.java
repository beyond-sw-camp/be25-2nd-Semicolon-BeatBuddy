package com.beyond.beatbuddy.music.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)  // 필요없는 필드 무시용 annotation
public class TrackAnalysisResponse {
	private int popularity;
	private int energy;
	private int danceability;
	private int happiness;
	private int acousticness;
	private int instrumentalness;
	private int liveness;
	private int speechiness;
}
