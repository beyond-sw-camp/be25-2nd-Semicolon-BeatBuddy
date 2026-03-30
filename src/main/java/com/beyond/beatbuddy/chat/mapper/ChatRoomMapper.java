package com.beyond.beatbuddy.chat.mapper;

import com.beyond.beatbuddy.chat.dto.response.ChatRoomResponse;
import com.beyond.beatbuddy.chat.entity.ChatRoom;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ChatRoomMapper {

    // 채팅방 조회
    @Select("""
        SELECT room_id, user_a_id, user_b_id, last_message_id, last_message_text, last_message_at, created_at
        FROM chat_rooms
        WHERE user_a_id = LEAST(#{loginUserId}, #{opponentUserId})
          AND user_b_id = GREATEST(#{loginUserId}, #{opponentUserId})
          AND room_id IN (
              SELECT room_id FROM chat_room_members
              WHERE user_id = #{loginUserId}
                AND is_exited = FALSE
          )
    """)
    Optional<ChatRoom> findByUsers(@Param("loginUserId") Long loginUserId,
                                   @Param("opponentUserId") Long opponentUserId);

    // 채팅방 생성
    @Insert("""
        INSERT INTO chat_rooms (user_a_id, user_b_id, created_at)
        VALUES (LEAST(#{userAId}, #{userBId}), GREATEST(#{userAId}, #{userBId}), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "roomId")
    void insertChatRoom(ChatRoom chatRoom);

    // 멤버 생성
    @Insert("""
        INSERT INTO chat_room_members (room_id, user_id, unread_count, is_exited, created_at)
        VALUES (#{roomId}, #{userId}, 0, FALSE, NOW())
    """)
    void insertChatRoomMember(@Param("roomId") Long roomId, @Param("userId") Long userId);

    // 반환
    @Select("""
    SELECT
        r.room_id,
        u.user_id AS opponent_user_id,
        u.nickname AS opponent_nickname,
        u.profile_image_url AS opponent_profile_image_url
    FROM chat_rooms r
    JOIN users u ON u.user_id = CASE
        WHEN r.user_a_id = #{loginUserId} THEN r.user_b_id
        ELSE r.user_a_id
    END
    WHERE r.room_id = #{roomId}
""")
    ChatRoomResponse findRoomWithOpponent(@Param("roomId") Long roomId,
                                          @Param("loginUserId") Long loginUserId);
}