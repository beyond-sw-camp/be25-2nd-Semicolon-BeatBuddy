package com.beyond.beatbuddy.music.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasteVectorResponse {
    private Double avgPopularity;
    private Double avgEnergy;
    private Double avgDanceability;
    private Double avgHappiness;
    private Double avgAcousticness;
    private Double avgInstrumentalness;
    private Double avgLiveness;
    private Double avgSpeechiness;

    private Double stdPopularity;
    private Double stdEnergy;
    private Double stdDanceability;
    private Double stdHappiness;
    private Double stdAcousticness;
    private Double stdInstrumentalness;
    private Double stdLiveness;
    private Double stdSpeechiness;
}
