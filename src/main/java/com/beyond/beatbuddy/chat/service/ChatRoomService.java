package com.beyond.beatbuddy.chat.service;

import com.beyond.beatbuddy.chat.dto.response.ChatMessageResponse;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomEnterResponse;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomListResponse;
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

import java.util.List;
import java.util.NoSuchElementException;
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

        // 그 유저 존재여부 확인

        Optional<ChatRoom> existingRoom = chatRoomMapper.findRoomByUsers(loginUserId, opponentUserId);
        if (existingRoom.isPresent()) {
            ChatRoomResponse response = chatRoomMapper.findChatRoomInfo(existingRoom.get().getRoomId(), loginUserId);
            return ApiResponse.of(HttpStatus.OK, "채팅방 조회 성공", response);
        }

        ChatRoom newRoom = new ChatRoom();
        newRoom.setUserAId(Math.min(loginUserId, opponentUserId));
        newRoom.setUserBId(Math.max(loginUserId, opponentUserId));
        chatRoomMapper.insertChatRoom(newRoom);
        chatRoomMapper.insertChatRoomMember(newRoom.getRoomId(), loginUserId);
        chatRoomMapper.insertChatRoomMember(newRoom.getRoomId(), opponentUserId);

        ChatRoomResponse response = chatRoomMapper.findChatRoomInfo(newRoom.getRoomId(), loginUserId);
        return ApiResponse.of(HttpStatus.CREATED, "채팅방이 생성되었습니다.", response);
    }

    @Transactional
    public ChatRoomEnterResponse enterChatRoom(Long roomId, Long loginUserId) {

        // 채팅방 존재 여부 확인
        if (chatRoomMapper.existsById(roomId) == 0) {
            throw new NoSuchElementException("채팅방을 찾을 수 없습니다.");
        }

        // 채팅방 접근 가능 여부 확인
        Long opponentUserId = chatRoomMapper.findOpponentUserId(roomId, loginUserId);
        if (opponentUserId == null) {
            throw new NoSuchElementException("채팅방에 접근할 수 없습니다.");
        }

        // 메시지 조회
        List<ChatMessageResponse> messages = chatRoomMapper.findMessages(roomId, loginUserId, opponentUserId);

        // 읽음 처리
        chatRoomMapper.updateReadStatus(roomId, loginUserId);

        return new ChatRoomEnterResponse(messages);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomListResponse> getChatRooms(Long loginUserId) {
        return chatRoomMapper.findAllByUserId(loginUserId);
    }
}

