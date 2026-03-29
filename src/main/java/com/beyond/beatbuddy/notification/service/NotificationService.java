package com.beyond.beatbuddy.notification.service;

import com.beyond.beatbuddy.notification.entity.Notification;
import com.beyond.beatbuddy.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    /**
     * 알림 읽음 처리 (NOTI_001)
     * - 본인의 알림만 읽음 처리 가능
     * - 이미 읽은 알림이어도 멱등성 보장 (중복 호출해도 200 반환)
     */
    @Transactional
    public void markAsRead(Long myUserId, Long notificationId) {
        Notification notification = notificationMapper.findById(notificationId);

        if (notification == null) {
            throw new NoSuchElementException("해당 알림을 찾을 수 없습니다.");
        }
        if (!notification.getUserId().equals(myUserId)) {
            throw new IllegalArgumentException("해당 알림에 접근할 권한이 없습니다.");
        }

        notificationMapper.markAsRead(notificationId);
    }
}
