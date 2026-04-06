package com.beyond.beatbuddy.chat.controller;

import com.beyond.beatbuddy.chat.dto.response.EventResponse;
import com.beyond.beatbuddy.chat.mapper.ChatRoomMapper;
import com.beyond.beatbuddy.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/read")
    public void markAsRead(Map<String, Long> payload, SimpMessageHeaderAccessor accessor) {
        Long loginUserId = (Long) accessor.getSessionAttributes().get("userId");
        Long roomId = payload.get("roomId");

        chatRoomService.markAsRead(roomId, loginUserId);

        Long opponentUserId = chatRoomMapper.findOpponentUserId(roomId, loginUserId);
        messagingTemplate.convertAndSend("/sub/events/" + opponentUserId,
                new EventResponse("MESSAGE_READ", roomId));

        log.info("읽음처리: roomId={}, loginUserId={}, opponentUserId={}", roomId, loginUserId, opponentUserId);
    }
}
