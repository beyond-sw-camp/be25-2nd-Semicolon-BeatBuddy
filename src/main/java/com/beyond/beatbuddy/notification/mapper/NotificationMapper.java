package com.beyond.beatbuddy.notification.mapper;

import com.beyond.beatbuddy.notification.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationMapper {

    // 알림 단건 조회
    Notification findById(@Param("notificationId") Long notificationId);

    // 읽음 처리
    void markAsRead(@Param("notificationId") Long notificationId);
}
