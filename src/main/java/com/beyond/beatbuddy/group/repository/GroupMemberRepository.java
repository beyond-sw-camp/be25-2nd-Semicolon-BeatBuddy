package com.beyond.beatbuddy.group.repository;

import com.beyond.beatbuddy.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    boolean existsByGroupIdAndGroupNickname(Long groupId, String groupNickname);

    List<GroupMember> findByUserId(Long userId);

    List<GroupMember> findByGroupId(Long groupId);

    void deleteByGroupIdAndUserId(Long groupId, Long userId);
}