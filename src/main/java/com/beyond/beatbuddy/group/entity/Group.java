package com.beyond.beatbuddy.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false, unique = true, length = 20)
    private String groupName;

    @Builder.Default
    @Column(nullable = false)
    private Integer memberCount = 0;

    @Column(nullable = false)
    private Long creatorId;

    @Column(length = 50)
    private String description;

    @Column(length = 500)
    private String groupImageUrl;

    @Column(nullable = false, unique = true, length = 20)
    private String inviteCode;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void addMember() {
        this.memberCount++;
    }

    public void removeMember() {
        if (this.memberCount > 0) {
            this.memberCount--;
        }
    }
}