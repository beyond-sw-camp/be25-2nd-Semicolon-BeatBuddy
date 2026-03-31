package com.beyond.beatbuddy.group.repository;

import com.beyond.beatbuddy.group.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMemberRepository {

    void save(GroupMember groupMember);

    boolean existsByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);

    boolean existsByGroupIdAndGroupNickname(@Param("groupId") Long groupId, @Param("groupNickname") String groupNickname);

    List<GroupMember> findByUserId(Long userId);

    List<GroupMember> findByGroupId(Long groupId);

    void deleteByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
}