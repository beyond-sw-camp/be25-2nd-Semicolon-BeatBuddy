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
8. [🧪 테스트 계획 및 결과 보고서](#테스트-계획-및-결과-보고서)

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

---

## 🔧 시스템 아키텍처

> <img width="6337" height="3477" alt="Web App Reference Architecture" src="https://github.com/user-attachments/assets/cdbc3a66-c663-4016-b9c5-fca6e740520d" />


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

---

## 🧪 테스트 계획 및 결과 보고서

> [테스트 계획 및 결과 보고서](https://docs.google.com/spreadsheets/d/1_yQBYho9SeX9E9EfWcyGfwN5yh79QyOxMhvVj6B-Qds/edit?gid=908489767#gid=908489767)

---


## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| Frontend | ![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white) |
| Backend | ![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) |
| Database | ![MariaDB](https://img.shields.io/badge/MariaDB(MySQL)-003545?style=for-the-badge&logo=mariadb&logoColor=white) |
| ORM | ![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge) |
| 인증 | ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) |
| 이메일 | ![Gmail](https://img.shields.io/badge/GmailSMTP-EA4335?style=for-the-badge&logo=gmail&logoColor=white) |
| 실시간 채팅 | ![WebSocket](https://img.shields.io/badge/WebSocket(STOMP)-010101?style=for-the-badge) |
| 외부 API | ![Spotify](https://img.shields.io/badge/SpotifyAPI-1DB954?style=for-the-badge&logo=spotify&logoColor=white)&nbsp;![RapidAPI](https://img.shields.io/badge/RapidAPI-0055DA?style=for-the-badge&logo=rapid&logoColor=white) |

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

#### 김예지
> 백엔드 프로젝트는 처음이라 하나하나 배워나가는 시간이었습니다. STOMP 방식의 WebSocket으로 채팅을 구현하면서, 처음엔 단순해 보였던 양방향 통신 구조에 생각보다 많은 예외 케이스가 있다는 걸 직접 부딪히며 깨달았습니다. 특히 STOMP 연결 시 HTTP와 다르게 인터셉터 단에서 JWT를 직접 파싱하고 Redis 블랙리스트까지 검증해야 했던 부분이 인상 깊었습니다. 채팅방을 퇴장한 이후 이전 메시지를 어떻게 숨길 것인지, 상대방의 읽음 처리를 언제 어떻게 전파할 것인지 등 처음에는 미처 생각하지 못했던 부분들을 하나씩 마주치면서 구조를 여러 번 다듬어 나갔습니다. 구현 중 가장 기억에 남는 것은 채팅 메시지 전송 시 발생한 데드락 문제였는데, 동시성 이슈를 직접 마주하고 원인을 파악해 해결해본 경험이 값졌습니다. 팀원 모두 웹 백엔드가 처음이었던 만큼 서로 모르는 부분을 함께 찾아가며 진행해야 했고 쉽지 않은 과정이었지만, 그만큼 실질적으로 많이 성장할 수 있었던 프로젝트였습니다.

#### 박하얀
> 이번 프로젝트를 통해 백엔드 개발을 처음 제대로 경험해볼 수 있었습니다. Spring Boot를 사용해서 기능을 만들고, 데이터베이스와 연결해서 데이터를 저장하거나 조회하는 과정이 처음에는 많이 어렵게 느껴졌습니다. 익숙하지 않은 개념도 많았고, 작은 기능 하나를 구현하는 데에도 생각보다 많은 과정이 필요하다는 걸 알게 되었습니다. 기능을 수정하면 예상하지 못했던 오류가 생기거나 다른 부분까지 영향을 주는 경우도 있어서, 개발은 단순히 코드를 작성하는 것이 아니라 계속 확인하고 수정해나가는 과정이라는 점을 느꼈습니다. 저는 음악 기능을 구현하였는데, 특히 Spotify API와 Rapid API를 연동하는 과정이 가장 어려웠고, 혼자서는 해결하기 힘든 부분도 많았습니다. 그럴 때마다 팀원들의 많은 도움을 받으며 해결해 나갈 수 있었습니다. 이번 프로젝트를 하면서 부족한 점도 많이 느꼈지만, 그만큼 실제 백엔드가 어떻게 동작하는지 배우고 경험할 수 있었던 의미 있는 시간이었습니다.

#### 방지혁
> 회고 작성 예정

#### 이다윗
> 회고 작성 예정

#### 허진호
> 이번 백엔드 프로젝트를 진행하면서 하나의 서비스가 완성되기까지 생각보다 훨씬 많은 고민과 노력, 그리고 다양한 어려움이 따른다는 것을 배울 수 있었습니다. 단순히 기능을 구현하는 것에서 끝나는 것이 아니라, 서비스의 흐름을 이해하고 여러 문제를 해결해 나가는 과정 하나하나가 중요하다는 점도 느낄 수 있었습니다. 비록 많이 부족했지만 이번 프로젝트를 통해 많은 것을 배우고 한 걸음 더 성장할 수 있었습니다. 그런 과정 속에서 끝까지 저를 이끌어주시고 아낌없이 도와주신 조장님과 팀원분들께 진심으로 감사드립니다.

#### 황희수
> 백엔드 개발을 처음 경험하면서 서로 맞물리는 구조 안에서 각 기능의 비즈니스 로직을 서비스 계층에 분리하는 것이 낯설고 어려웠지만, 직접 설계하고 구현하는 과정에서 구조적 사고를 기를 수 있었습니다. 오류 발생 시 로그를 먼저 확인하여 오류를 파악하는 습관을 통해 문제 해결 능력을 키우고자 노력했습니다. 또한, 팀 협업에서 Git 브랜치 전략과 병합 전 검토 과정의 중요성을 체감할 수 있었습니다. 기능 구현에 집중하다 보니 엔티티 간 연관관계 설계나 코드 리팩토링까지 충분히 신경 쓰지 못한 점이 아쉬움으로 남습니다. 다음 프로젝트에서는 초기 설계 단계에 더 많은 시간을 투자하여 완성도를 높이고 싶습니다.

![footer](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=20&height=100&section=footer)
