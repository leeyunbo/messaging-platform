package com.messaging.common.dto

/**
 * API 응답 코드
 */
enum class ResponseCode(
    val code: String,
    val message: String
) {
    // 성공
    SUCCESS("0000", "성공"),

    // 클라이언트 에러 (E0XX)
    INVALID_PARAMETER("E001", "파라미터 오류"),
    UNAUTHORIZED("E002", "인증 실패"),
    RATE_LIMIT_EXCEEDED("E003", "요청 한도 초과"),
    DUPLICATE_REQUEST("E004", "중복 요청"),
    INVALID_MESSAGE_TYPE("E005", "유효하지 않은 메시지 타입"),
    CONTENT_TOO_LONG("E006", "메시지 길이 초과"),
    PARTNER_NOT_FOUND("E007", "파트너 정보 없음"),
    PARTNER_INACTIVE("E008", "비활성 파트너"),

    // 서버 에러 (E1XX)
    INTERNAL_ERROR("E100", "시스템 오류"),
    MQ_ERROR("E101", "메시지 큐 오류"),
    DB_ERROR("E102", "데이터베이스 오류")
}
