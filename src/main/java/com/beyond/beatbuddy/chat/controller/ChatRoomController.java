package com.beyond.beatbuddy.chat.controller;

import com.beyond.beatbuddy.chat.dto.request.CreateChatRoomRequest;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomEnterResponse;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomListResponse;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomResponse;
import com.beyond.beatbuddy.chat.service.ChatRoomService;
import com.beyond.beatbuddy.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;


    // 채팅룸 생성
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createChatRoom(@RequestBody CreateChatRoomRequest req) {
        Long loginUserId = 1L;
        return chatRoomService.createChatRoom(loginUserId, req.getOpponentUserId());
    }

    //.채팅방 입장
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<ChatRoomEnterResponse>> enterChatRoom(@PathVariable Long roomId) {
        Long loginUserId = 1L; // TODO: 임시 하드코딩
        return ApiResponse.of(HttpStatus.OK, "조회 성공", chatRoomService.enterChatRoom(roomId, loginUserId));
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomListResponse>>> getChatRooms() {
        Long loginUserId = 1L; // TODO: 임시 하드코딩 - JWT 연동 시 교체
        return ApiResponse.of(HttpStatus.OK, "채팅방 목록 조회 성공", chatRoomService.getChatRooms(loginUserId));
    }

}