# ADR-002: UseCase/Bootstrap 모듈명 통일

## 상태
승인됨 (2025-01)

## 컨텍스트

카카오 발송은 두 가지 방식이 존재:
- **알림톡**: 카카오 비즈메시지 API 사용
- **브랜드메시지**: 카카오 다이렉트 API 사용

기존 모듈명이 불명확했음:

```
message-usecase/
├── kakao-usecase/         # 알림톡? 전체?
└── kakao-direct-usecase/  # 다이렉트가 뭔지 모름
```

## 결정

**제품명 기준으로 모듈명을 통일한다.**

```
message-usecase/
├── kakao-alimtalk-usecase/      # 알림톡
└── kakao-brandmessage-usecase/  # 브랜드메시지

message-bootstrap/
├── kakao-alimtalk-sender/
└── kakao-brandmessage-sender/
```

**패키지명도 모듈명과 일치시킨다.**

```kotlin
// Before
package com.messaging.usecase.kakao
package com.messaging.usecase.kakaodirect

// After
package com.messaging.usecase.alimtalk
package com.messaging.usecase.brandmessage
```

## 결과

**장점**
- 모듈명만 보고 어떤 제품인지 파악 가능
- 패키지명과 모듈명 일관성
- 신규 입사자도 이해하기 쉬움

**변경 목록**
| Before | After |
|--------|-------|
| kakao-usecase | kakao-alimtalk-usecase |
| kakao-direct-usecase | kakao-brandmessage-usecase |
| com.messaging.usecase.kakao | com.messaging.usecase.alimtalk |
| com.messaging.usecase.kakaodirect | com.messaging.usecase.brandmessage |
| com.messaging.bootstrap.kakao | com.messaging.bootstrap.alimtalk |
| com.messaging.bootstrap.kakaodirect | com.messaging.bootstrap.brandmessage |
