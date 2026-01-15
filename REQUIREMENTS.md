# Messaging Platform 요구사항 정의서

메시징 게이트웨이 시스템으로, 다양한 발송 타입(SMS, 카카오, RCS 등)을 통합 관리한다.

---

## 1. 시스템 개요

### 1.1 모듈 구성

| 모듈 | 역할 |
|------|------|
| receiver | HTTP API, 인증, 멱등성 체크, MQ Push |
| sender | MQ 수신, 검증, 발송 이력 DB 저장, Provider API 호출 |
| reporter | 결과 MQ 수신, Webhook 발송, 발송 결과 조회 API |
| provider | 벤더사 연동 추상화, Mock 구현 |
| webhook-receiver | 비동기 결과 수신 (RCS, SMS, MMS, KAKAO 폴링) |
| common | 도메인, DTO, 공통 유틸 |

---

## 2. 비즈니스 정책

### 2.1 발송 정책

| 항목 | 정책 |
|------|------|
| 대체 발송 | 미지원 (파트너 관심사) |
| 예약 발송 | 미지원 (파트너 관심사) |
| 발송 전 취소 | 미지원 (파트너 관심사) |
| 재시도 | 최대 3회, 지수 백오프, ConnectionTimeout 시에만 |
| 재시도 방식 | 별도 리트라이 큐 사용 |
| 중복 발송 방지 | partnerId + clientMsgId 유니크 제약 |

### 2.2 메시지 타입

| 타입 | 최대 길이 | 길이 기준 |
|------|----------|----------|
| SMS | 90 | BYTE |
| LMS | 4000 | BYTE |
| MMS | 2000 | BYTE |
| KAKAO_ALIMTALK | 1000 | CHARACTER |
| KAKAO_BRAND_MESSAGE | 1400 | CHARACTER |
| NAVER_TALK | 1000 | CHARACTER |
| RCS | 1300 | BYTE |

### 2.3 메시지 라이프사이클

| 항목 | 정책 |
|------|------|
| 메시지 보관 기간 | 90일 |
| 상태 값 | RECEIVED, PROCESSING, SENT, DELIVERED, FAILED, EXPIRED |

### 2.4 통계

| 항목 | 정책 |
|------|------|
| 집계 단위 | 일별 |
| 집계 기준 | 파트너별, 메시지 타입별 |
| 집계 항목 | 총 발송건수, 성공건수, 실패건수 |
| 집계 방식 | 이벤트 기반 (리포트 처리 시 업데이트) |

---

## 3. 인증/보안

### 3.1 인증 방식

| 항목 | 정책 |
|------|------|
| 인증 방식 | API Key (clientId + clientSecret) -> JWT 발급 |
| Access Token 만료 | 1일 |
| Refresh Token | 지원 |
| API Key 구조 | 파트너당 1개 |

### 3.2 Rate Limit

| 항목 | 정책 |
|------|------|
| 적용 단위 | 파트너별 |
| 기본값 | 1000 TPS |
| 초과 시 응답 | E003 (요청 한도 초과) |

---

## 4. 메시지 흐름

### 4.1 MQ 구조

| 항목 | 정책 |
|------|------|
| 발송 큐 | 타입별 분리 (send.sms.queue, send.kakao.queue 등) |
| 리트라이 큐 | 공통 (send.retry.queue) |
| 리포트 큐 | 공통 (report.queue) |
| MQ 장애 시 | Circuit Breaker + 503 응답 |

### 4.2 Provider 연동

| 벤더 | 응답 방식 |
|------|----------|
| 카카오 | 즉시 응답 또는 폴링 (Webhook 결과) |
| 네이버톡톡 | 즉시 응답 |
| RCS | 접수 응답 + Webhook 결과 |
| SMS/LMS/MMS | 접수 응답 + Webhook 결과 |

### 4.3 결과 전달 (Webhook)

| 항목 | 정책 |
|------|------|
| Webhook | 지원 |
| Webhook 인증 | HMAC-SHA256 서명 + Timestamp |
| Webhook 실패 시 | DLQ 적재 |
| 조회 API | 단건 조회 (1일간 제공) |

#### Webhook 헤더

| 헤더 | 설명 |
|------|------|
| X-Signature | HMAC-SHA256(webhookSecret, timestamp + "." + body) |
| X-Timestamp | Unix timestamp (초) |

---

## 5. 데이터

### 5.1 채번

| 항목 | 정책 |
|------|------|
| 채번 주체 | 서버 (TSID) |
| 형식 | 13자리 Base32 문자열 |
| 특징 | 시간순 정렬 가능, 분산 환경 충돌 없음 |

### 5.2 테이블 구조

#### messages

| 항목 | 정책 |
|------|------|
| 용도 | 발송/리포트 단일 테이블 |
| 상세 정보 | JSON 컬럼 (detail) |

| 인덱스 | 컬럼 |
|--------|------|
| PK | messageId |
| idx_message_partner_id | partnerId |
| idx_message_status | status |
| idx_message_created_at | createdAt |

| 유니크 제약 | 컬럼 |
|-------------|------|
| uk_message_partner_client_msg | partnerId, clientMsgId |

#### partner_daily_stats

| 항목 | 정책 |
|------|------|
| 용도 | 파트너별 일별 발송 통계 |
| 집계 기준 | partnerId, statDate, messageType |

| 인덱스 | 컬럼 |
|--------|------|
| idx_stat_partner_id | partnerId |
| idx_stat_date | statDate |

| 유니크 제약 | 컬럼 |
|-------------|------|
| uk_stat_partner_date_type | partnerId, statDate, messageType |

### 5.3 응답 코드

| 코드 | 설명 |
|------|------|
| 0000 | 성공 |
| E001 | 파라미터 오류 |
| E002 | 인증 실패 |
| E003 | 요청 한도 초과 |
| E004 | 중복 요청 |
| E005 | 유효하지 않은 메시지 타입 |
| E006 | 메시지 길이 초과 |
| E007 | 파트너 정보 없음 |
| E008 | 비활성 파트너 |
| E100 | 시스템 오류 |
| E101 | 메시지 큐 오류 |
| E102 | 데이터베이스 오류 |

---

## 6. 기술 스택

| 항목 | 기술 |
|------|------|
| 언어 | Kotlin 2.3.0 |
| 프레임워크 | Spring Boot 4.0.1, WebFlux |
| 리액티브 | Reactor, Coroutines |
| DB | JPA, MySQL |
| MQ | RabbitMQ (reactor-rabbitmq) |
| 캐시 | Redis (멱등성 체크) |
| 인증 | JWT (jjwt) |
| 빌드 | Gradle 8.14 |
