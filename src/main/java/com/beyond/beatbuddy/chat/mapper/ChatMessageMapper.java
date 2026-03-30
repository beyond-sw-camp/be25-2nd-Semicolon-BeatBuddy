package com.beyond.beatbuddy.chat.mapper;

import com.beyond.beatbuddy.chat.entity.ChatMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChatMessageMapper {

    // 메시지 저장
    @Insert("""
        INSERT INTO chat_messages (room_id, sender_id, message_text, created_at)
        VALUES (#{roomId}, #{senderId}, #{messageText}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void insertMessage(ChatMessage chatMessage);

    // chat_rooms last_message 업데이트
    @Update("""
        UPDATE chat_rooms
        SET last_message_id = #{messageId},
            last_message_text = #{messageText},
            last_message_at = NOW()
        WHERE room_id = #{roomId}
    """)
    void updateLastMessage(@Param("messageId") Long messageId,
                           @Param("messageText") String messageText,
                           @Param("roomId") Long roomId);

    // 상대방 unread_count 증가
    @Update("""
        UPDATE chat_room_members
        SET unread_count = unread_count + 1
        WHERE room_id = #{roomId}
          AND user_id <> #{senderId}
          AND is_exited = FALSE
    """)
    void incrementUnreadCount(@Param("roomId") Long roomId,
                              @Param("senderId") Long senderId);

    // 전송한 메시지 반환
    @Select("""
        SELECT message_id, room_id, sender_id, message_text, created_at
        FROM chat_messages
        WHERE message_id = #{messageId}
    """)
    @Results({
            @Result(property = "messageId", column = "message_id"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "senderId", column = "sender_id"),
            @Result(property = "messageText", column = "message_text"),
            @Result(property = "createdAt", column = "created_at")
    })
    ChatMessage findById(@Param("messageId") Long messageId);
}