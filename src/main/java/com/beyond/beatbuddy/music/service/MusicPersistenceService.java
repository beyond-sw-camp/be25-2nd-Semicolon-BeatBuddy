package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.music.dto.request.SaveTasteRequest;
import com.beyond.beatbuddy.music.dto.response.TrackAnalysisResponse;
import com.beyond.beatbuddy.music.entity.Music;
import com.beyond.beatbuddy.music.mapper.MusicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MusicPersistenceService {

    private final MusicMapper musicMapper;
    private final UserProfileService userProfileService;

    // 신규 저장용 트랜잭션
    @Transactional
    public void saveNewTaste(Long userId,
                             List<SaveTasteRequest.TrackInfo> tracks,
                             Map<String, TrackAnalysisResponse> featureMap,
                             String tasteVector) {

        persistCommon(userId, tracks, featureMap, false);
        userProfileService.saveWithRetry(userId,tasteVector);
        musicMapper.updateIsTasteAnalyzed(userId);
    }

    // 수정 저장용 트랜잭션
    @Transactional
    public void updateExistingTaste(Long userId,
                                    List<SaveTasteRequest.TrackInfo> tracks,
                                    Map<String, TrackAnalysisResponse> featureMap,
                                    String tasteVector) {

        persistCommon(userId, tracks, featureMap, true);
        userProfileService.saveWithRetry(userId, tasteVector);
        musicMapper.updateIsTasteAnalyzed(userId);
    }

    // 공통 저장 로직
    private void persistCommon(Long userId,
                               List<SaveTasteRequest.TrackInfo> tracks,
                               Map<String, TrackAnalysisResponse> featureMap,
                               boolean deleteOldFavorites) {

        if (deleteOldFavorites) {
            musicMapper.deleteUserFavMusic(userId);
        }

        for (SaveTasteRequest.TrackInfo track : tracks) {
            TrackAnalysisResponse features = featureMap.get(track.getTrackId());

            musicMapper.insertAlbumIgnore(
                    track.getAlbumId(),
                    track.getAlbumName(),
                    track.getCoverUrl()
            );

            musicMapper.insertMusicFeaturesIgnore(track, features);
        }

        // user_fav_music 개별 insert -> batch insert
        List<String> musicIds = tracks.stream()
                .map(SaveTasteRequest.TrackInfo::getTrackId)
                .toList();

        musicMapper.insertUserFavMusicBatch(userId, musicIds);
    }

}
