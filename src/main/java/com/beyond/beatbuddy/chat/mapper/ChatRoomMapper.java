package com.beyond.beatbuddy.chat.mapper;

import com.beyond.beatbuddy.chat.dto.response.ChatMessageResponse;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomListResponse;
import com.beyond.beatbuddy.chat.dto.response.ChatRoomResponse;
import com.beyond.beatbuddy.chat.entity.ChatRoom;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
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
    Optional<ChatRoom> findRoomByUsers(@Param("loginUserId") Long loginUserId,
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
    ChatRoomResponse findChatRoomInfo(@Param("roomId") Long roomId,
                                          @Param("loginUserId") Long loginUserId);

    //채팅방 존재 여부 확인
    @Select("SELECT COUNT(*) FROM chat_rooms WHERE room_id = #{roomId}")
    int existsById(@Param("roomId") Long roomId);

    // CHAT_002-1 메시지 조회
    @Select("""
        SELECT
            m.message_id,
            m.sender_id,
            u.nickname AS sender_nickname,
            m.message_text,
            m.created_at,
            CASE
                WHEN m.sender_id = #{loginUserId}
                 AND m.message_id > (
                     SELECT COALESCE(crm.last_read_message_id, 0)
                     FROM chat_room_members crm
                     WHERE crm.room_id = #{roomId}
                       AND crm.user_id = #{opponentUserId}
                 )
                THEN FALSE
                WHEN m.sender_id = #{loginUserId}
                THEN TRUE
                ELSE NULL
            END AS is_read
        FROM chat_messages m
        JOIN users u ON u.user_id = m.sender_id
        WHERE m.room_id = #{roomId}
        ORDER BY m.message_id ASC
    """)
    List<ChatMessageResponse> findMessages(@Param("roomId") Long roomId,
                                           @Param("loginUserId") Long loginUserId,
                                           @Param("opponentUserId") Long opponentUserId);

    // CHAT_002-2 읽음 처리
    @Update("""
        UPDATE chat_room_members
        SET last_read_message_id = (
                SELECT cr.last_message_id
                FROM chat_rooms cr
                WHERE cr.room_id = #{roomId}
            ),
            unread_count = 0,
            last_read_at = NOW()
        WHERE room_id = #{roomId}
          AND user_id = #{loginUserId}
    """)
    void updateReadStatus(@Param("roomId") Long roomId,
                          @Param("loginUserId") Long loginUserId);

    // 접근 가능 여부 확인 => 상대방 userId 반환
    @Select("""
    SELECT\s
        CASE
            WHEN user_a_id = #{loginUserId} THEN user_b_id
            WHEN user_b_id = #{loginUserId} THEN user_a_id
            ELSE NULL
        END AS opponent_user_id
    FROM chat_rooms
    WHERE room_id = #{roomId}
""")
    Long findOpponentUserId(@Param("roomId") Long roomId,
                            @Param("loginUserId") Long loginUserId);


    // 채팅방 목록 조회
    @Select("""
    SELECT
        r.room_id,
        CASE
            WHEN r.user_a_id = #{loginUserId} THEN r.user_b_id
            ELSE r.user_a_id
        END AS opponent_user_id,
        u.nickname AS opponent_nickname,
        u.profile_image_url AS opponent_profile_image_url,
        r.last_message_text,
        r.last_message_at,
        m.unread_count
    FROM chat_rooms r
    JOIN chat_room_members m ON r.room_id = m.room_id
    JOIN users u ON u.user_id = CASE
        WHEN r.user_a_id = #{loginUserId} THEN r.user_b_id
        ELSE r.user_a_id
    END
    WHERE m.user_id = #{loginUserId}
      AND m.is_exited = FALSE
      AND r.last_message_at IS NOT NULL
    ORDER BY r.last_message_at DESC, r.room_id DESC
""")
    List<ChatRoomListResponse> findAllByUserId(@Param("loginUserId") Long loginUserId);

}