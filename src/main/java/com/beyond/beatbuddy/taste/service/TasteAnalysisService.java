package com.beyond.beatbuddy.taste.service;

import com.beyond.beatbuddy.music.entity.MusicFeature;
import com.beyond.beatbuddy.music.entity.UserFavMusic;
import com.beyond.beatbuddy.music.mapper.MusicFeatureMapper;
import com.beyond.beatbuddy.music.mapper.UserFavMusicMapper;
import com.beyond.beatbuddy.taste.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TasteAnalysisService {

    private final UserFavMusicMapper userFavMusicMapper;
    private final MusicFeatureMapper musicFeatureMapper;
    private final UserProfileMapper userProfileMapper;

    /**
     * 사용자 최애곡 10곡을 바탕으로 8차원 취향 벡터 계산 후 저장
     * 벡터 구조: [energy, danceability, happiness, acousticness,
     *             instrumentalness, liveness, speechiness, popularity] 의 평균 (0.0~1.0)
     */
    @Transactional
    public void analyze(Long userId) {
        List<UserFavMusic> favList = userFavMusicMapper.findByUserId(userId);
        if (favList.size() < 10) {
            throw new IllegalStateException("취향 분석을 위해 최애곡 10곡이 필요합니다.");
        }

        double[] energy           = new double[10];
        double[] danceability     = new double[10];
        double[] happiness        = new double[10];
        double[] acousticness     = new double[10];
        double[] instrumentalness = new double[10];
        double[] liveness         = new double[10];
        double[] speechiness      = new double[10];
        double[] popularity       = new double[10];

        for (int i = 0; i < 10; i++) {
            MusicFeature mf = musicFeatureMapper.findById(favList.get(i).getMusicId());
            energy[i]           = normalize(mf.getEnergy());
            danceability[i]     = normalize(mf.getDanceability());
            happiness[i]        = normalize(mf.getHappiness());
            acousticness[i]     = normalize(mf.getAcousticness());
            instrumentalness[i] = normalize(mf.getInstrumentalness());
            liveness[i]         = normalize(mf.getLiveness());
            speechiness[i]      = normalize(mf.getSpeechiness());
            popularity[i]       = normalize(mf.getPopularity());
        }

        // 8차원 벡터: 각 특성의 평균값만 사용 (VECTOR(8) 스키마 기준)
        double[] vector = new double[8];
        vector[0] = mean(energy);
        vector[1] = mean(danceability);
        vector[2] = mean(happiness);
        vector[3] = mean(acousticness);
        vector[4] = mean(instrumentalness);
        vector[5] = mean(liveness);
        vector[6] = mean(speechiness);
        vector[7] = mean(popularity);

        // MariaDB VEC_FromText('[v1,v2,...,v8]') 형식으로 저장
        String vectorStr = toVectorString(vector);
        userProfileMapper.upsert(userId, vectorStr);

        log.info("[TasteAnalysis] userId={} 취향 분석 완료: {}", userId, vectorStr);
    }

    /** 0~100 → 0.0~1.0 정규화 */
    private double normalize(Long value) {
        return value == null ? 0.0 : value / 100.0;
    }

    private double mean(double[] arr) {
        double sum = 0;
        for (double v : arr) sum += v;
        return sum / arr.length;
    }

    /** VEC_FromText 인자 형식: "[v1,v2,v3,v4,v5,v6,v7,v8]" */
    private String toVectorString(double[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(String.format("%.6f", vector[i]));
            if (i < vector.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
