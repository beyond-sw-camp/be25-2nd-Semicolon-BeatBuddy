package com.beyond.beatbuddy.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String senderNickname;
    private String messageText;
    private LocalDateTime createdAt;
    private Boolean isRead;
}