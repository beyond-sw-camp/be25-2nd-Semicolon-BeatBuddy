package com.beyond.beatbuddy.taste.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long userId;
    private double[] tasteVector;   // 16차원: [평균8, 표준편차8]
    private LocalDateTime updatedAt;
}
