package com.beyond.beatbuddy.group.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupResponse {
    private Long groupId;
    private String groupName;
    private String description;
    private Integer memberCount;
    private String groupImageUrl;
}