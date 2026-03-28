package com.beyond.beatbuddy.group.repository;

import com.beyond.beatbuddy.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByGroupName(String groupName);

    boolean existsByInviteCode(String inviteCode);

    Optional<Group> findByInviteCode(String inviteCode);
}