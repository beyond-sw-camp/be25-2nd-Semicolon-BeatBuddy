-- 사용자 제공 스키마 명세서 기반 전체 DDL

-- =========================================================
-- 기존 테이블 삭제 (외래키 제약조건 고려하여 역순으로 삭제)
-- =========================================================
DROP TABLE IF EXISTS `chat_messages`;
DROP TABLE IF EXISTS `chat_room_members`;
DROP TABLE IF EXISTS `chat_rooms`;
DROP TABLE IF EXISTS `notifications`;
DROP TABLE IF EXISTS `friendships`;
DROP TABLE IF EXISTS `viewed_profiles`;
DROP TABLE IF EXISTS `group_members`;
DROP TABLE IF EXISTS `user_groups`;
DROP TABLE IF EXISTS `user_profiles`;
DROP TABLE IF EXISTS `user_fav_music`;
DROP TABLE IF EXISTS `music_features`;
DROP TABLE IF EXISTS `albums`;
DROP TABLE IF EXISTS `email_verifications`;
DROP TABLE IF EXISTS `users`;

-- =========================================================
-- 테이블 생성
-- =========================================================

-- 1. users
CREATE TABLE `users` (
    `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100) UNIQUE,
    `password` VARCHAR(255) COMMENT '암호화 저장, 카카오 유저 NULL 가능',
    `kakao_id` VARCHAR(255) UNIQUE,
    `nickname` VARCHAR(50) NOT NULL,
    `gender` ENUM('MALE','FEMALE'),
    `birth_year` BIGINT,
    `profile_image_url` VARCHAR(500) COMMENT '미선택 시 기본 이미지',
    `provider` VARCHAR(20) COMMENT 'LOCAL, KAKAO',
    `is_tutorial_viewed` BOOLEAN NOT NULL DEFAULT FALSE,
    `is_taste_analyzed` BOOLEAN NOT NULL DEFAULT FALSE,
    `status` ENUM('ACTIVE','DELETED') NOT NULL DEFAULT 'ACTIVE',
    `allow_push_chat` BOOLEAN NOT NULL DEFAULT TRUE,
    `allow_push_social` BOOLEAN NOT NULL DEFAULT TRUE,
    `refresh_token` VARCHAR(500),
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. email_verifications
CREATE TABLE `email_verifications` (
    `verification_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100) NOT NULL,
    `auth_code` VARCHAR(6) NOT NULL,
    `attempt_count` BIGINT NOT NULL DEFAULT 0 COMMENT '5회 제한',
    `expires_at` TIMESTAMP NOT NULL,
    `is_verified` BOOLEAN NOT NULL DEFAULT FALSE,
    `last_attempt_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_email_verifications_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. albums
CREATE TABLE `albums` (
    `album_id` VARCHAR(50) PRIMARY KEY COMMENT 'Spotify Album ID',
    `album_name` VARCHAR(255) NOT NULL,
    `album_cover_url` VARCHAR(500) COMMENT 'Spotify 이미지 URL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. music_features
CREATE TABLE `music_features` (
    `music_id` VARCHAR(50) PRIMARY KEY COMMENT 'Spotify Track ID',
    `album_id` VARCHAR(50),
    `track_name` VARCHAR(255) NOT NULL,
    `artist_name` VARCHAR(255) COMMENT '복수 아티스트 시 콤마 구분',
    `popularity` BIGINT COMMENT '0~100',
    `energy` BIGINT COMMENT '0~100',
    `danceability` BIGINT COMMENT '0~100',
    `happiness` BIGINT COMMENT '0~100 (Valence)',
    `acousticness` BIGINT COMMENT '0~100',
    `instrumentalness` BIGINT COMMENT '0~100',
    `liveness` BIGINT COMMENT '0~100',
    `speechiness` BIGINT COMMENT '0~100',
    CONSTRAINT `fk_music_features_album` FOREIGN KEY (`album_id`) REFERENCES `albums` (`album_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. user_fav_music
CREATE TABLE `user_fav_music` (
    `fav_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `music_id` VARCHAR(50) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_fav_music` (`user_id`, `music_id`),
    CONSTRAINT `fk_user_fav_music_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_fav_music_music` FOREIGN KEY (`music_id`) REFERENCES `music_features` (`music_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. user_profiles
CREATE TABLE `user_profiles` (
    `user_id` BIGINT PRIMARY KEY,
    `taste_vector` VECTOR(16) NOT NULL COMMENT '16차원: [평균8, 표준편차8], 0.0~1.0 정규화',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    VECTOR INDEX `idx_taste_vector` (`taste_vector`),
    CONSTRAINT `fk_user_profiles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. user_groups
CREATE TABLE `user_groups` (
    `group_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `group_name` VARCHAR(20) NOT NULL UNIQUE,
    `member_count` BIGINT NOT NULL DEFAULT 0,
    `creator_id` BIGINT,
    `description` VARCHAR(50),
    `group_image_url` VARCHAR(500),
    `invite_code` VARCHAR(20) NOT NULL UNIQUE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_user_groups_creator` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. group_members
CREATE TABLE `group_members` (
    `member_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `group_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `group_nickname` VARCHAR(50) NOT NULL COMMENT '그룹 내 UNIQUE',
    `joined_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_group_members_group_user` (`group_id`, `user_id`),
    CONSTRAINT `fk_group_members_group` FOREIGN KEY (`group_id`) REFERENCES `user_groups` (`group_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_group_members_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. viewed_profiles
CREATE TABLE `viewed_profiles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `viewer_id` BIGINT NOT NULL,
    `viewed_id` BIGINT NOT NULL,
    `viewed_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_viewed_profiles` (`viewer_id`, `viewed_id`),
    CONSTRAINT `fk_viewed_profiles_viewer` FOREIGN KEY (`viewer_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_viewed_profiles_viewed` FOREIGN KEY (`viewed_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. friendships
CREATE TABLE `friendships` (
    `friendship_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `requester_id` BIGINT NOT NULL,
    `receiver_id` BIGINT NOT NULL,
    `status` ENUM('PENDING','ACCEPTED') NOT NULL DEFAULT 'PENDING',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_friendships` (`requester_id`, `receiver_id`),
    CONSTRAINT `fk_friendships_requester` FOREIGN KEY (`requester_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_friendships_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. notifications
CREATE TABLE `notifications` (
    `notification_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `sender_id` BIGINT,
    `type` ENUM('FRIEND_REQUEST','FRIEND_ACCEPT', 'TOTAL_SYSTEM') NOT NULL,
    `target_id` BIGINT COMMENT 'room_id 또는 group_id',
    `message` VARCHAR(255) NOT NULL,
    `is_read` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_notifications_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. chat_rooms
CREATE TABLE `chat_rooms` (
    `room_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_a_id` BIGINT NOT NULL,
    `user_b_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_chat_rooms_users` (`user_a_id`, `user_b_id`),
    CHECK (`user_a_id` < `user_b_id`),
    CONSTRAINT `fk_chat_rooms_usera` FOREIGN KEY (`user_a_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_chat_rooms_userb` FOREIGN KEY (`user_b_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. chat_room_members
CREATE TABLE `chat_room_members` (
    `room_member_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `room_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `unread_count` BIGINT NOT NULL DEFAULT 0,
    `is_exited` BOOLEAN NOT NULL DEFAULT FALSE,
    `last_read_at` TIMESTAMP NULL,
    UNIQUE KEY `uk_chat_room_members` (`room_id`, `user_id`),
    CONSTRAINT `fk_chat_room_members_room` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`room_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_chat_room_members_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. chat_messages
CREATE TABLE `chat_messages` (
    `message_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `room_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `message_text` TEXT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_chat_messages_room` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`room_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_chat_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
