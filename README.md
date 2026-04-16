# ![header](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=20&height=200&animation=twinkling&section=header&text=BeatBuddy&fontSize=80&fontAlign=75&fontAlignY=36)

# BeatBuddy - 음악 취향 기반 친목 커뮤니티 서비스

## 👀 목차
1. [👥 팀원](#팀원)
2. [📚 프로젝트 개요](#프로젝트-개요)
3. [🔧 시스템 아키텍처](#시스템-아키텍처)
4. [📅 요구사항 명세서](#요구사항-명세서)
5. [🪧 ERD](#erd)
6. [🗃️ 테이블 명세서](#테이블-명세서)
7. [🎯 API 명세서](#api-명세서)

---

### 👥 팀원

| 팀원1 | 팀원2 | 팀원3 | 팀원4 | 팀원5 | 팀원6 |
| --- | --- | --- | --- | --- | --- |
| 김예지 | 박하얀 | 방지혁 | 이다윗 | 허진호 | 황희수

## 📌 프로젝트 개요

### 📘 프로젝트 소개

**BeatBuddy**는 소속 집단 구성원들의 음악적 취향 데이터를 분석하여 정서적 공감대가 높은 동료를 연결해 주는 팀 빌딩 지원 플랫폼입니다.

가입 시 최애곡 10곡을 선택하면, 곡의 음악적 특성을 분석하여 16차원 취향 벡터를 생성합니다. 같은 그룹에 속한 멤버들과 코사인 유사도를 계산해 취향이 비슷한 사람을 추천받고, 마음에 드는 상대에게 친구 신청을 보내 1:1 채팅으로 대화할 수 있습니다.

---

### ✅ 배경: 왜 이 서비스가 필요한가?

**취향 기반 연결의 필요성**

기존 소셜 서비스는 외형이나 조건 중심의 매칭으로 인해 실제 깊이 있는 정서적 교감을 기대하기 어렵고, 이는 곧 대화의 단절과 관계 유지를 위한 심리적 피로감으로 이어집니다.

**음악이 가진 연결의 힘**

음악 취향은 성격, 감성, 라이프스타일과 높은 상관관계를 가집니다. 같은 곡을 좋아한다는 사실만으로도 대화의 물꼬를 트기 쉬워집니다.

**그룹 기반 안전한 만남**

그룹 안에서만 추천이 이루어지므로, 불특정 다수와의 무작위 연결이 아닌 신뢰할 수 있는 환경에서 친구를 사귈 수 있습니다.



## 🔧 시스템 아키텍처

```
[Vue.js Frontend]
       ↕ HTTP / WebSocket
[Spring Boot Backend]
       ↕ MyBatis
[MariaDB Database]
       
[외부 API]
- Spotify API : 곡 검색, 앨범 커버
- SoundNet API : 음악 특성 데이터
- Gmail SMTP : 이메일 인증
```

---

## 📋 요구사항 명세서
> [요구사항 명세서](https://docs.google.com/spreadsheets/d/1bW-t6EJQ1NW_uqkoiAgVqqeyeDuuh2muPwpv8ee4Y2s/edit?usp=sharing)

> <img width="1253" height="616" alt="0006" src="https://github.com/user-attachments/assets/a673d195-2814-4cab-886d-d116ac2a5081" />

> <img width="1253" height="627" alt="0007" src="https://github.com/user-attachments/assets/ad49f3fb-ba88-427a-a232-f29d37eb51dc" />

> <img width="1253" height="203" alt="0008" src="https://github.com/user-attachments/assets/c3b66b10-d6c9-4647-a729-af592188945c" />

>
---

## 🪧 ERD
> [ERD](https://www.erdcloud.com/d/MEMRtro3g2PtxsnCE)

> <img width="1480" height="789" alt="erd" src="https://github.com/user-attachments/assets/0843a848-2e35-4366-b78b-3ede51581168" />

---

## 🗃️ 테이블 명세서
> [테이블 명세서](https://docs.google.com/spreadsheets/d/1bW-t6EJQ1NW_uqkoiAgVqqeyeDuuh2muPwpv8ee4Y2s/edit?usp=sharing)

> <img width="1592" height="597" alt="0001" src="https://github.com/user-attachments/assets/1bba87ee-df74-443b-aea1-00cbdd34d545" />

> <img width="1592" height="628" alt="0002" src="https://github.com/user-attachments/assets/3a107a6c-a74c-4e3a-a036-8da812653d8b" />

> <img width="1592" height="590" alt="0003" src="https://github.com/user-attachments/assets/0459cda1-1201-4640-a5e6-1e1384a6baab" />

> <img width="1592" height="630" alt="0004" src="https://github.com/user-attachments/assets/0ad86793-67ed-4615-a390-52d8dc7f72f8" />

> <img width="1592" height="570" alt="0005" src="https://github.com/user-attachments/assets/3fae7b04-e835-46cc-82dc-51357f913fc6" />


**총 14개 테이블**
`users` `email_verifications` `albums` `music_features` `user_fav_music` `user_profiles` `user_groups` `group_members` `viewed_profiles` `friendships` `chat_rooms` `chat_room_members` `chat_messages` `notifications`

---

## 🎯 API 명세서

> [API 명세서](https://www.notion.so/5-API-f76901cdc00882208183014b29afe442?source=copy_link)



---

## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| Frontend | Vue.js |
| Backend | Spring Boot |
| Database | MariaDB |
| ORM | MyBatis |
| 인증 | JWT |
| 이메일 | Gmail SMTP |
| 실시간 채팅 | WebSocket (STOMP) |
| 외부 API | Spotify API, SoundNet API |

---

## 📌 주요 기술적 의사결정

**취향 벡터 설계**
단순 평균이 아닌 평균 + 표준편차를 함께 저장하는 16차원 벡터를 설계했습니다. 표준편차를 포함함으로써 "취향의 폭"까지 반영할 수 있어 매칭 정확도를 높였습니다.

**viewed_profiles 설계**
Redis 대신 DB 테이블로 구현하되 `ON DUPLICATE KEY UPDATE`를 활용해 넘긴 프로필을 30일 후 재노출하는 로직을 구현했습니다. MVP 이후 Redis로의 교체를 고려한 구조입니다.

**채팅방 중복 방지**
`user_a_id < user_b_id` CHECK 제약 조건과 UNIQUE KEY를 조합해 두 사용자 간 채팅방이 중복 생성되는 것을 DB 레벨에서 원천 차단했습니다.

---

### 회고록

#### 팀원1
> 회고 작성 예정

#### 팀원2
> 회고 작성 예정

#### 팀원3
> 회고 작성 예정

#### 팀원4
> 회고 작성 예정

#### 팀원5
> 회고 작성 예정

#### 팀원6
> 회고 작성 예정

![footer](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=20&height=100&section=footer)
