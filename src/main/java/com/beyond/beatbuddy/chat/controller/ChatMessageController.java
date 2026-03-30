package com.beyond.beatbuddy.chat.controller;

import com.beyond.beatbuddy.chat.dto.request.ChatMessageRequest;
import com.beyond.beatbuddy.chat.entity.ChatMessage;
import com.beyond.beatbuddy.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageRequest req, SimpMessageHeaderAccessor accessor) {

        Long senderId = (Long) accessor.getSessionAttributes().get("userId");

        // 메시지 저장
        ChatMessage chatMessage = chatMessageService.sendMessage(
                req.getRoomId(),
                senderId,
                req.getMessageText()
        );

        // 구독자들한테 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/rooms/" + req.getRoomId(),
                chatMessage
        );
    }
}
