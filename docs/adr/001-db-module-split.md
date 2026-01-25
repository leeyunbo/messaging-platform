# ADR-001: Infrastructure DB 모듈 분리

## 상태
승인됨 (2025-01)

## 컨텍스트

기존에 `message-infrastructure:db` 단일 모듈에 모든 발송 채널의 Repository가 존재했음.

```
message-infrastructure/
└── db/
    ├── SmsRepository
    ├── AlimtalkPollingRepository
    ├── RcsRepository
    └── NaverRepository
```

**문제**: `kakao-brandmessage-sender`를 실행했는데 `AlimtalkPollingRepository` 빈을 찾을 수 없다는 에러 발생.
- 브랜드메시지는 알림톡 폴링이 필요 없음
- 하지만 db 모듈을 의존하면 알림톡 관련 빈도 스캔됨
- 채널 간 불필요한 의존성 발생

## 결정

DB 모듈을 발송 채널별로 분리한다.

```
message-infrastructure/
├── db-sms/
├── db-kakao/
├── db-rcs/
└── db-naver/
```

## 결과

**장점**
- 채널별 독립적인 DB 의존성 관리
- 불필요한 빈 로딩 방지
- 채널 추가 시 기존 모듈에 영향 없음

**단점**
- 모듈 수 증가 (4개 추가)
- 공통 DB 설정이 있다면 중복 가능성

**대안 검토**
- `@Profile`로 분리: "왜 다른 레이어는 모듈로 분리하고 DB만 Profile인가?" 일관성 문제
- `@ConditionalOnProperty`: 설정 복잡도 증가

→ 모듈 분리가 가장 명확하고 일관된 방식
