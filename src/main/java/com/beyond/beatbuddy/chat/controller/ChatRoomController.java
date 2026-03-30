package com.beyond.beatbuddy.chat.controller;

import com.beyond.beatbuddy.chat.dto.request.CreateChatRoomRequest;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomResponse;
import com.beyond.beatbuddy.chat.service.ChatRoomService;
import com.beyond.beatbuddy.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}