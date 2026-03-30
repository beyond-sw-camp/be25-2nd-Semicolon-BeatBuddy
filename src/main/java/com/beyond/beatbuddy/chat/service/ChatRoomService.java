package com.beyond.beatbuddy.chat.service;

import com.beyond.beatbuddy.chat.dto.response.ChatRoomResponse;
import com.beyond.beatbuddy.chat.entity.ChatRoom;
import com.beyond.beatbuddy.chat.mapper.ChatRoomMapper;
import com.beyond.beatbuddy.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomMapper chatRoomMapper;

    public ResponseEntity<ApiResponse<ChatRoomResponse>> createChatRoom(Long loginUserId, Long opponentUserId) {
        if (loginUserId.equals(opponentUserId)) {
            throw new IllegalArgumentException("자기 자신에게 채팅을 보낼 수 없습니다.");
        }

        Optional<ChatRoom> existingRoom = chatRoomMapper.findByUsers(loginUserId, opponentUserId);
        if (existingRoom.isPresent()) {
            ChatRoomResponse response = chatRoomMapper.findRoomWithOpponent(existingRoom.get().getRoomId(), loginUserId);
            return ApiResponse.of(HttpStatus.OK, "채팅방 조회 성공", response);
        }

        ChatRoom newRoom = new ChatRoom();
        newRoom.setUserAId(Math.min(loginUserId, opponentUserId));
        newRoom.setUserBId(Math.max(loginUserId, opponentUserId));
        chatRoomMapper.insertChatRoom(newRoom);
        chatRoomMapper.insertChatRoomMember(newRoom.getRoomId(), loginUserId);
        chatRoomMapper.insertChatRoomMember(newRoom.getRoomId(), opponentUserId);

        ChatRoomResponse response = chatRoomMapper.findRoomWithOpponent(newRoom.getRoomId(), loginUserId);
        return ApiResponse.of(HttpStatus.CREATED, "채팅방이 생성되었습니다.", response);
    }
}

