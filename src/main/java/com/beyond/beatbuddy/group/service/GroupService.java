package com.beyond.beatbuddy.group.service;

import com.beyond.beatbuddy.global.exception.ConflictException;
import com.beyond.beatbuddy.global.exception.NotFoundException;
import com.beyond.beatbuddy.group.dto.GroupCreateRequest;
import com.beyond.beatbuddy.group.dto.GroupResponse;
import com.beyond.beatbuddy.group.entity.Group;
import com.beyond.beatbuddy.group.entity.GroupMember;
import com.beyond.beatbuddy.group.repository.GroupMemberRepository;
import com.beyond.beatbuddy.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public boolean checkGroupNameDuplicate(String groupName) {

        return groupRepository.existsByGroupName(groupName);
    }

    public boolean checkInviteCodeDuplicate(String inviteCode) {

        return groupRepository.existsByInviteCode(inviteCode.toUpperCase());
    }

    @Value("${app.group.default-image-url}")
    private String defaultGroupImageUrl;

    @Transactional
    public Long createGroup(GroupCreateRequest request, Long creatorId) {

        if (groupRepository.existsByGroupName(request.getGroupName())) {

            throw new ConflictException("이미 존재하는 그룹명입니다.");
        }

        String inviteCode = request.getInviteCode().toUpperCase();

        if (groupRepository.existsByInviteCode(inviteCode)) {

            throw new ConflictException("이미 사용 중인 초대 코드입니다.");
        }

        String groupImageUrl = request.getGroupImageUrl() != null
                ? request.getGroupImageUrl() : defaultGroupImageUrl;

        Group group = Group.builder()
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .inviteCode(inviteCode)
                .groupImageUrl(groupImageUrl)
                .creatorId(creatorId)
                .build();

        group.addMember();

        Group savedGroup = groupRepository.save(group);

        GroupMember firstMember = GroupMember.builder()
                .groupId(savedGroup.getGroupId())
                .userId(creatorId)
                .groupNickname(request.getGroupNickname())
                .build();

        groupMemberRepository.save(firstMember);

        return savedGroup.getGroupId();
    }

    public GroupResponse getGroupByInviteCode(String inviteCode) {
        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 초대코드입니다."));

        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .groupImageUrl(group.getGroupImageUrl())
                .memberCount(group.getMemberCount())
                .build();
    }

    public boolean isNicknameDuplicate(Long groupId, String nickname) {
        if (!groupRepository.existsById(groupId)) {

            throw new NotFoundException("존재하지 않는 그룹입니다.");
        }

        return groupMemberRepository.existsByGroupIdAndGroupNickname(groupId, nickname);
    }
}