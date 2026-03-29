package com.beyond.beatbuddy.group.mapper;

import com.beyond.beatbuddy.group.dto.RecommendationResponseDto;
import com.beyond.beatbuddy.friend.entity.ViewedProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecommendationMapper {

    // 스킵(열람) 이력 저장 → viewed_profiles
    void insertViewedProfile(ViewedProfile viewedProfile);

    // 그룹 내 취향 유사도 기반 추천 목록 조회
    List<RecommendationResponseDto> findRecommendations(
            @Param("myUserId") Long myUserId,
            @Param("groupId") Long groupId);
}
