# 아키텍처 상세 설명

## 개요
이 프로젝트는 **클린 아키텍처(Clean Architecture)** 와 **모노레포(Monorepo)** 를 결합한 구조

1. 클린 아키텍처
- 비즈니스 로직이 외부 API, DB, 프레임워크 등 기술적 세부사항에 의존하지 않도록 설계
- 즉, 의존성 방향이 일반적인 아키텍처와 다르게 안쪽 레이어가 바깥쪽 레이어를 알지 못함
- 이를 통해 비즈니스 로직의 독립성, 테스트 용이성, 변경 영향 최소화 등의 이점을 얻음

2. 모노레포 
- 조직에서 관리하는 n개의 프로젝트를 하나의 저장소에서 관리
- 코드 공유가 용이하고, 모든 프로젝트의 Dependency 버전을 일관되게 유지 가능함
- 조직의 모든 구성원들이 같은 코드베이스를 보고 이해할 수 있어 커뮤니케이션이 원활해짐

3. 클린 아키텍처 + 모노레포 
- 두가지 접근법의 장점이 합쳐져, **"통합하되 독립적인"** 시스템을 구축 가능
- 공유할 건 공유하고, 분리할 건 분리하여 유지보수성과 확장성을 극대화
- 팀 전체가 같은 코드베이스를 바라보며, 죽어있던 코드 리뷰/지식 공유 문화가 되살아남

---

## 레이어 구조
![Clean+Monorepo.png](diagram/Clean%2BMonorepo.png)

---

## 각 레이어 상세

### 1. core ({message}-domain)

**역할**: 순수한 도메인 모델과 비즈니스 규칙 정의

**특징**
- 외부 프레임워크 의존성 없음 (Spring, JPA 등 X)
- 순수 Kotlin으로 작성
- 포트(Port) 인터페이스 정의

**구성**
```
message-core/
├── partner-domain/     # 고객사 정보
├── report-domain/      # 발송 결과 리포트
├── sms-domain/         # SMS Bounded Context
├── kakao-domain/       # 카카오 Bounded Context
├── rcs-domain/         # RCS Bounded Context
└── naver-domain/       # 네이버 Bounded Context
```

**의존성 규칙**
- 다른 레이어를 의존하지 않음
- library만 의존 가능 (로깅 등 횡단 관심사)

---

### 2. UseCase ({messageType}-usecase)

**역할**: 사용자 시나리오 단위의 비즈니스 로직 조율

**특징**
- core의 포트 인터페이스를 사용
- 구체적인 구현체는 알지 못함
- 트랜잭션 경계, 비즈니스 흐름 제어

**구성**
```
message-usecase/
├── sms-usecase/
├── lms-mms-usecase/
├── kakao-alimtalk-usecase/
├── kakao-brandmessage-usecase/
├── rcs-usecase/
└── naver-usecase/
```
**의존성 규칙**
- core 의존
- platform, infrastructure의 구체 클래스 의존 X (인터페이스만)

---

### 3. Platform ({vendor}-platform)

**역할**: 외부 플랫폼 API 연동 (Outbound Adapter)

**특징**
- Core의 포트 인터페이스 구현
- 외부 API 스펙에 맞게 요청/응답 변환
- CircuitBreaker, Retry 등 장애 격리 처리

**구성**
```
message-platform/
├── skt-platform/           # SKT SMS API
├── kt-platform/            # KT SMS API
├── lgt-platform/           # LGT SMS API
├── kakao-platform/         # 카카오 비즈메시지 API
├── kakao-direct-platform/  # 카카오 다이렉트 API
└── naver-platform/         # 네이버 API
```
**의존성 규칙**
- core 의존 (포트 구현을 위해)
- infrastructure 의존 (WebClient 등 기술 구현체 사용)

---

### 4. Infrastructure

**역할**: 기술적 세부사항 구현

**특징**
- DB, 메시지 큐, 네트워크 등 기술 구현
- Core의 포트 인터페이스 구현 (Repository 등)
- 프레임워크/라이브러리 직접 사용

**구성**
```
message-infrastructure/
├── db-sms/        # SMS R2DBC Repository
├── db-kakao/      # 카카오 R2DBC Repository
├── db-rcs/        # RCS R2DBC Repository
├── db-naver/      # 네이버 R2DBC Repository
├── rabbitmq/      # RabbitMQ 설정 및 Publisher
└── netty/         # Netty Pipeline 설정
```

**의존성 규칙**
- core 의존 (포트 구현을 위해)
- 기술 라이브러리 직접 의존 (R2DBC, Spring AMQP 등)

---

### 5. Library

**역할**: 모든 모듈에서 공통으로 사용하는 횡단 관심사

**특징**
- 특정 도메인에 종속되지 않음
- 유틸리티 성격
- 모든 레이어에서 의존 가능

**구성**
```
message-library/
├── id-generator/   # 메시지 ID 생성
└── logging/        # 공통 로깅
```
---

### 6. Bootstrap

**역할**: 의존성 조립 + 애플리케이션 진입점

**특징**
- 모든 레이어에 의존하며, 각 레이어를 조립하여 하나의 독립적인 애플리케이션 구성
- Spring Boot Application 진입점
- 채널별 독립 배포 단위
- Controller, Consumer 등 Inbound Adapter 포함

**구성**
```
message-bootstrap/
├── sms-receiver/
├── sms-sender/
├── lms-mms-receiver/
├── lms-mms-sender/
├── kakao-receiver/
├── kakao-alimtalk-sender/
├── kakao-brandmessage-sender/
├── rcs-receiver/
├── rcs-sender/
├── naver-receiver/
├── naver-sender/
└── reporter/
```

**의존성 규칙**
- 모든 레이어 의존 가능 (조립 역할)
- 비즈니스 로직 작성 X

**왜 Inbound Adapter가 Bootstrap에?**

원칙적으로 Inbound Adapter(Controller, Consumer)는 별도 레이어로 분리하는 게 맞음.
하지만 채널별로 Inbound Adapter 모듈을 만들면 모듈 수가 과도하게 늘어남.

```
# 분리할 경우
message-inbound/
├── sms-inbound/
├── kakao-inbound/
├── rcs-inbound/
└── naver-inbound/
```

실용적인 선택으로 Bootstrap에 Inbound Adapter를 포함시킴.
대신 다음 원칙은 지킴
- Consumer/Controller에서 외부 형식 → 도메인 객체 변환
- UseCase는 외부 DTO를 알지 못함 (도메인 객체만 받음)
- 비즈니스 로직은 UseCase에서 처리

---

## Bounded Context 분리

각 발송 채널은 독립된 Bounded Context로 분리
- "Message"라는 개념이 각 컨텍스트마다 다르게 정의됨 
- SMS : 수신번호 + 발신번호 + 텍스트
- 카카오 알림톡 : 수신번호 + 발신프로필 + {Chat Bubble}
- RCS : 수신번호 + 발신번호 + {Rich Media}
- Naver : 수신번호 + 계정 + {Naver Message}
- 각 컨텍스트는 자신만의 도메인 모델과 비즈니스 규칙을 가짐

### 분리 방식

```
message-core/
├── sms-domain/      
├── kakao-domain/   
├── rcs-domain/     
└── partner-domain/  # 공유 컨텍스트 (고객사 정보)
```

각 도메인은 자신만의 **Ubiquitous Language**로 모델링됨

---

## 변경 영향 범위

이 구조의 최고의 장점은 하나로 통합되었지만 **변경 영향이 최소화**된다는 것

- 만약 SKT API 스펙이 변경된다면?
  - `platform:skt` 모듈만 수정하면 됨
  - `platform:skt`를 사용하는 `bootstrap:sms-sender`만 영향이 있음 
- 만약 카카오 알림톡 API 스펙이 변경된다면?
  - `platform:kakao` 모듈만 수정하면 됨
  - `platform:kakao`를 사용하는 `bootstrap:kakao-alimtalk-sender`만 영향이 있음
- 특히, 통신사가 운영하는 SMS, LMS, MMS, RCS API에 비해 카카오, 네이버 API는 자주 변경되기 때문에 변경주기가 다름
  - 이 구조를 통해 각기다른 변경주기를 가진 채널들을 통합하였지만 변경 영향 없이 독립적으로 관리 가능
- 만약 새로운 발송 채널이 추가된다면?
  - 기존 모듈들을 재사용하여 공통 기능을 손쉽게 추가 가능하며, 독립적으로 구성이 가능하기 때문에 기존 채널에 영향 없음
