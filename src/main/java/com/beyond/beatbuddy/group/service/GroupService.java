package com.beyond.beatbuddy.group.service;

import com.beyond.beatbuddy.global.exception.BadRequestException;
import com.beyond.beatbuddy.global.exception.ConflictException;
import com.beyond.beatbuddy.global.exception.NotFoundException;
import com.beyond.beatbuddy.group.dto.GroupCreateRequest;
import com.beyond.beatbuddy.group.dto.GroupJoinRequest;
import com.beyond.beatbuddy.group.dto.GroupResponse;
import com.beyond.beatbuddy.group.entity.Group;
import com.beyond.beatbuddy.group.entity.GroupMember;
import com.beyond.beatbuddy.group.mapper.GroupMemberMapper;
import com.beyond.beatbuddy.group.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupMapper groupMapper;
    private final GroupMemberMapper groupMemberMapper;

    @Value("${app.group.default-image-url}")
    private String defaultGroupImageUrl;

    public boolean checkGroupNameDuplicate(String groupName) {
        return groupMapper.existsByGroupName(groupName);
    }

    public boolean checkInviteCodeDuplicate(String inviteCode) {
        return groupMapper.existsByInviteCode(inviteCode.toUpperCase());
    }

    @Transactional
    public Long createGroup(GroupCreateRequest request, Long creatorId) {

        if (groupMapper.existsByGroupName(request.getGroupName())) {
            throw new ConflictException("이미 존재하는 그룹명입니다.");
        }

        String inviteCode = request.getInviteCode().toUpperCase();

        if (groupMapper.existsByInviteCode(inviteCode)) {
            throw new ConflictException("이미 사용 중인 초대 코드입니다.");
        }

        String groupImageUrl = request.getGroupImageUrl() != null
                ? request.getGroupImageUrl()
                : defaultGroupImageUrl;

        Group group = Group.builder()
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .inviteCode(inviteCode)
                .groupImageUrl(groupImageUrl)
                .creatorId(creatorId)
                .memberCount(1)
                .build();

        groupMapper.save(group);

        GroupMember firstMember = GroupMember.builder()
                .groupId(group.getGroupId())
                .userId(creatorId)
                .groupNickname(request.getGroupNickname())
                .build();

        groupMemberMapper.save(firstMember);

        return group.getGroupId();
    }

    public GroupResponse getGroupByInviteCode(String inviteCode) {
        Group group = groupMapper.findByInviteCode(inviteCode)
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
        if (!groupMapper.existsById(groupId)) {
            throw new NotFoundException("존재하지 않는 그룹입니다.");
        }
        return groupMemberMapper.existsByGroupIdAndGroupNickname(groupId, nickname);
    }

    @Transactional
    public Long joinGroup(Long groupId, GroupJoinRequest request, Long userId) {

        Group group = groupMapper.findById(groupId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 그룹입니다."));

        if (!group.getInviteCode().equals(request.getInviteCode().toUpperCase())) {
            throw new BadRequestException("초대코드가 올바르지 않습니다.");
        }

        if (groupMemberMapper.existsByGroupIdAndUserId(groupId, userId)) {
            throw new ConflictException("이미 가입한 그룹입니다.");
        }

        if (groupMemberMapper.existsByGroupIdAndGroupNickname(groupId, request.getGroupNickname())) {
            throw new ConflictException("이미 사용 중인 닉네임입니다.");
        }

        GroupMember member = GroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .groupNickname(request.getGroupNickname())
                .build();

        groupMemberMapper.save(member);

        groupMapper.updateMemberCount(groupId, group.getMemberCount() + 1);

        return groupId;
    }

    public List<GroupResponse> getMyGroups(Long userId) {
        List<Group> groups = groupMapper.findGroupsByUserId(userId);

        return groups.stream()
                .map(group -> GroupResponse.builder()
                        .groupId(group.getGroupId())
                        .groupName(group.getGroupName())
                        .description(group.getDescription())
                        .memberCount(group.getMemberCount())
                        .groupImageUrl(group.getGroupImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}