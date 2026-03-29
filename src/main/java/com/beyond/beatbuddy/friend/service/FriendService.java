package com.beyond.beatbuddy.friend.service;

import com.beyond.beatbuddy.friend.dto.FriendDetailResponseDto;
import com.beyond.beatbuddy.friend.dto.FriendRequestDto;
import com.beyond.beatbuddy.friend.dto.FriendResponseDto;
import com.beyond.beatbuddy.friend.entity.Friendship;
import com.beyond.beatbuddy.friend.mapper.FriendMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendMapper friendMapper;

    /**
     * 친구 요청 보내기
     * - 자기 자신에게 요청 불가
     * - 이미 PENDING 요청이 있는 경우 불가 (양방향)
     * - 이미 ACCEPTED 친구인 경우 불가
     */
    @Transactional
    public void sendFriendRequest(Long requesterId, FriendRequestDto dto) {
        Long receiverId = dto.getReceiverId();

        if (requesterId.equals(receiverId)) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        if (friendMapper.findPendingRequest(requesterId, receiverId) != null) {
            throw new DuplicateKeyException("이미 처리 중인 친구 요청이 존재합니다.");
        }

        if (friendMapper.findAcceptedFriendship(requesterId, receiverId) != null) {
            throw new DuplicateKeyException("이미 친구 관계입니다.");
        }

        Friendship friendship = Friendship.builder()
                .requesterId(requesterId)
                .receiverId(receiverId)
                .build();
        friendMapper.insertRequest(friendship);
    }

    /**
     * 친구 요청 수락
     * - 본인이 수신자인 PENDING 요청만 수락 가능
     */
    @Transactional
    public void acceptFriendRequest(Long myUserId, Long friendshipId) {
        Friendship friendship = getFriendshipOrThrow(friendshipId);

        if (!friendship.getReceiverId().equals(myUserId)) {
            throw new IllegalArgumentException("해당 요청을 수락할 권한이 없습니다.");
        }
        if (!"PENDING".equals(friendship.getStatus())) {
            throw new IllegalStateException("수락할 수 없는 상태의 요청입니다.");
        }

        friendMapper.updateStatus(friendshipId, "ACCEPTED");
    }

    /**
     * 친구 요청 거절
     * - 본인이 수신자인 PENDING 요청만 거절 가능
     * - 거절 시 row 자체를 삭제
     */
    @Transactional
    public void rejectFriendRequest(Long myUserId, Long friendshipId) {
        Friendship friendship = getFriendshipOrThrow(friendshipId);

        if (!friendship.getReceiverId().equals(myUserId)) {
            throw new IllegalArgumentException("해당 요청을 거절할 권한이 없습니다.");
        }
        if (!"PENDING".equals(friendship.getStatus())) {
            throw new IllegalStateException("거절할 수 없는 상태의 요청입니다.");
        }

        friendMapper.deleteFriend(friendshipId);
    }

    /**
     * 내 친구 목록 조회 (ACCEPTED, 양방향)
     */
    @Transactional(readOnly = true)
    public List<FriendResponseDto> getMyFriends(Long userId) {
        return friendMapper.findFriendsByUserId(userId);
    }

    /**
     * 친구 상세 정보 조회
     */
    @Transactional(readOnly = true)
    public FriendDetailResponseDto getFriendDetail(Long myUserId, Long friendId) {
        FriendDetailResponseDto detail = friendMapper.findFriendDetail(myUserId, friendId);
        if (detail == null) {
            throw new NoSuchElementException("친구 정보를 찾을 수 없습니다.");
        }
        return detail;
    }

    /**
     * 친구 삭제 (차단)
     * - 나와 관련된 friendship만 삭제 가능
     */
    @Transactional
    public void deleteFriend(Long myUserId, Long friendshipId) {
        Friendship friendship = getFriendshipOrThrow(friendshipId);

        boolean isParticipant = friendship.getRequesterId().equals(myUserId)
                || friendship.getReceiverId().equals(myUserId);
        if (!isParticipant) {
            throw new IllegalArgumentException("해당 친구 관계를 삭제할 권한이 없습니다.");
        }

        friendMapper.deleteFriend(friendshipId);
    }

    private Friendship getFriendshipOrThrow(Long friendshipId) {
        Friendship friendship = friendMapper.findById(friendshipId);
        if (friendship == null) {
            throw new NoSuchElementException("해당 친구 요청을 찾을 수 없습니다.");
        }
        return friendship;
    }
}
