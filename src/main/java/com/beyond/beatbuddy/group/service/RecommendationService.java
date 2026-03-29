package com.beyond.beatbuddy.group.service;

import com.beyond.beatbuddy.group.dto.RecommendationResponseDto;
import com.beyond.beatbuddy.friend.entity.ViewedProfile;
import com.beyond.beatbuddy.group.mapper.RecommendationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationMapper recommendationMapper;

    /**
     * 그룹 내 취향 기반 친구 추천 목록 조회
     */
    @Transactional(readOnly = true)
    public List<RecommendationResponseDto> getRecommendations(Long myUserId, Long groupId) {
        return recommendationMapper.findRecommendations(myUserId, groupId);
    }

    /**
     * 추천 친구 스킵 처리
     * - viewed_profiles에 기록하여 이후 추천에서 제외
     */
    @Transactional
    public void skipRecommendation(Long myUserId, Long targetUserId) {
        if (myUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("자기 자신을 스킵할 수 없습니다.");
        }

        ViewedProfile viewedProfile = ViewedProfile.builder()
                .viewerId(myUserId)
                .viewedId(targetUserId)
                .build();

        recommendationMapper.insertViewedProfile(viewedProfile);
    }
}
