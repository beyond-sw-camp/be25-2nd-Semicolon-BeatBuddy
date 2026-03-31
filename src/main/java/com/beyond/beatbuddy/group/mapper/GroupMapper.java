package com.beyond.beatbuddy.group.repository;

import com.beyond.beatbuddy.group.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

@Mapper
public interface GroupRepository {
    void save(Group group);

    Optional<Group> findById(Long groupId);

    boolean existsByGroupName(String groupName);

    boolean existsByInviteCode(String inviteCode);

    Optional<Group> findByInviteCode(String inviteCode);

    void update(Group group);
}