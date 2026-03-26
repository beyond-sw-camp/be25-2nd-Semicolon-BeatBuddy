-- ============================================================
--  BeatBuddy - 친구 기능 DDL
--  담당 기능: GROUP_005~007, FRIEND_001~007
--  DB: MariaDB 11.8+
--  작성일: 2026-03-11
--  기준: 테이블 명세서 v1.0
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS chat_messages;
DROP TABLE IF EXISTS chat_room_members;
DROP TABLE IF EXISTS chat_rooms;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS friend_skips;

DROP TABLE IF EXISTS friendships;
DROP TABLE IF EXISTS viewed_profiles;
DROP TABLE IF EXISTS group_members;
DROP TABLE IF EXISTS user_groups;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS user_fav_music;
DROP TABLE IF EXISTS music_features;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS email_verifications;
DROP TABLE IF EXISTS `users`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `users` (
    user_id            BIGINT       NOT NULL AUTO_INCREMENT,
    email              VARCHAR(100)     NULL,
    password           VARCHAR(255)     NULL,
    kakao_id           VARCHAR(255)     NULL,
    nickname           VARCHAR(50)  NOT NULL,
    gender             ENUM('MALE','FEMALE') NULL,
    birth_year         BIGINT           NULL,
    profile_image_url  VARCHAR(500)     NULL,
    is_tutorial_viewed BOOLEAN      NOT NULL DEFAULT FALSE,
    is_taste_analyzed  BOOLEAN      NOT NULL DEFAULT FALSE,
    status             ENUM('ACTIVE','DELETED') NOT NULL DEFAULT 'ACTIVE',
    allow_push_chat    BOOLEAN      NOT NULL DEFAULT TRUE,
    allow_push_social  BOOLEAN      NOT NULL DEFAULT TRUE,
    refresh_token      VARCHAR(500)     NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_users_email    (email),
    UNIQUE KEY uq_users_kakao_id (kakao_id),
    INDEX idx_users_status       (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE email_verifications (
    verification_id  BIGINT      NOT NULL AUTO_INCREMENT,
    email            VARCHAR(100) NOT NULL,
    auth_code        VARCHAR(6)  NOT NULL,
    attempt_count    BIGINT      NOT NULL DEFAULT 0,
    expires_at       TIMESTAMP   NOT NULL,
    is_verified      BOOLEAN     NOT NULL DEFAULT FALSE,
    last_attempt_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (verification_id),
    INDEX idx_email_verifications_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE albums (
    album_id        VARCHAR(50)  NOT NULL,
    album_name      VARCHAR(255) NOT NULL,
    album_cover_url VARCHAR(500)     NULL,
    PRIMARY KEY (album_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE music_features (
    music_id         VARCHAR(50)  NOT NULL,
    track_name       VARCHAR(255) NOT NULL,
    artist_name      VARCHAR(255)     NULL,
    album_id         VARCHAR(50)      NULL,
    popularity       BIGINT           NULL,
    energy           BIGINT           NULL,
    danceability     BIGINT           NULL,
    happiness        BIGINT           NULL,
    acousticness     BIGINT           NULL,
    instrumentalness BIGINT           NULL,
    liveness         BIGINT           NULL,
    speechiness      BIGINT           NULL,
    PRIMARY KEY (music_id),
    CONSTRAINT fk_music_features_album
        FOREIGN KEY (album_id) REFERENCES albums (album_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_fav_music (
    fav_id     BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    music_id   VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (fav_id),
    UNIQUE KEY uq_user_fav_music (user_id, music_id),
    INDEX idx_user_fav_music_user_id (user_id),
    CONSTRAINT fk_user_fav_music_user  FOREIGN KEY (user_id)  REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_fav_music_music FOREIGN KEY (music_id) REFERENCES music_features (music_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_profiles (
    user_id      BIGINT    NOT NULL,
    taste_vector VECTOR(8) NOT NULL,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_groups (
    group_id        BIGINT       NOT NULL AUTO_INCREMENT,
    group_name      VARCHAR(20)  NOT NULL,
    member_count    BIGINT       NOT NULL DEFAULT 0,
    creator_id      BIGINT           NULL,
    description     VARCHAR(50)      NULL,
    group_image_url VARCHAR(500)     NULL,
    invite_code     VARCHAR(20)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (group_id),
    UNIQUE KEY uq_user_groups_invite_code (invite_code),
    CONSTRAINT fk_user_groups_creator FOREIGN KEY (creator_id) REFERENCES `users` (user_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE group_members (
    member_id      BIGINT      NOT NULL AUTO_INCREMENT,
    group_id       BIGINT      NOT NULL,
    user_id        BIGINT      NOT NULL,
    group_nickname VARCHAR(50) NOT NULL,
    joined_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id),
    UNIQUE KEY uq_group_members (group_id, user_id),
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES user_groups (group_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_group_members_user  FOREIGN KEY (user_id)  REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE viewed_profiles (
    id        BIGINT    NOT NULL AUTO_INCREMENT,
    viewer_id BIGINT    NOT NULL,
    viewed_id BIGINT    NOT NULL,
    viewed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_viewed_profiles_viewer (viewer_id),
    CONSTRAINT fk_viewed_profiles_viewer FOREIGN KEY (viewer_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_viewed_profiles_viewed FOREIGN KEY (viewed_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE friendships (
    friendship_id BIGINT    NOT NULL AUTO_INCREMENT,
    requester_id  BIGINT    NOT NULL,
    receiver_id   BIGINT    NOT NULL,
    status        ENUM('PENDING','ACCEPTED') NOT NULL DEFAULT 'PENDING',
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (friendship_id),
    UNIQUE KEY uq_friendships (requester_id, receiver_id),
    INDEX idx_friendships_receiver (receiver_id),
    CONSTRAINT fk_friendships_requester FOREIGN KEY (requester_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_friendships_receiver  FOREIGN KEY (receiver_id)  REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE notifications (
    notification_id BIGINT       NOT NULL AUTO_INCREMENT,
    user_id         BIGINT       NOT NULL,
    content         VARCHAR(255) NOT NULL,
    type            ENUM('FRIEND_REQUEST','CHAT','SOCIAL','SYSTEM') NOT NULL,
    is_read         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id),
    INDEX idx_notifications_user_id    (user_id),
    INDEX idx_notifications_created_at (created_at),
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE chat_rooms (
    room_id    BIGINT    NOT NULL AUTO_INCREMENT,
    user_a_id  BIGINT    NOT NULL,
    user_b_id  BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id),
    UNIQUE KEY uq_chat_rooms (user_a_id, user_b_id),
    CONSTRAINT fk_chat_rooms_user_a FOREIGN KEY (user_a_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chat_rooms_user_b FOREIGN KEY (user_b_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE chat_room_members (
    room_member_id BIGINT    NOT NULL AUTO_INCREMENT,
    room_id        BIGINT    NOT NULL,
    user_id        BIGINT    NOT NULL,
    unread_count   BIGINT    NOT NULL DEFAULT 0,
    is_exited      BOOLEAN   NOT NULL DEFAULT FALSE,
    last_read_at   TIMESTAMP     NULL,
    PRIMARY KEY (room_member_id),
    UNIQUE KEY uq_chat_room_members (room_id, user_id),
    CONSTRAINT fk_chat_room_members_room FOREIGN KEY (room_id)  REFERENCES chat_rooms (room_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chat_room_members_user FOREIGN KEY (user_id)  REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE chat_messages (
    message_id BIGINT    NOT NULL AUTO_INCREMENT,
    room_id    BIGINT    NOT NULL,
    sender_id  BIGINT    NOT NULL,
    content    TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id),
    INDEX idx_chat_messages_room (room_id),
    CONSTRAINT fk_chat_messages_room   FOREIGN KEY (room_id)   REFERENCES chat_rooms (room_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chat_messages_sender FOREIGN KEY (sender_id) REFERENCES `users` (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
