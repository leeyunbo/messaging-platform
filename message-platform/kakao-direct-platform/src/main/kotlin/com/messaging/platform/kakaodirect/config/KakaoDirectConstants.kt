package com.messaging.platform.kakaodirect.config

/**
 * 카카오 다이렉트 브랜드메시지 API 상수
 */
object KakaoDirectApi {
    // HTTP 헤더
    const val HEADER_API_KEY = "X-Api-Key"
    const val HEADER_SECRET_KEY = "X-Secret-Key"
    const val HEADER_SERIAL_NUMBER = "X-Serial-Number"
}

/**
 * 카카오 다이렉트 API 에러 코드
 */
enum class KakaoDirectErrorCode(
    val code: String,
    val description: String
) {
    INVALID_PARAMETER("E001", "잘못된 파라미터"),
    INVALID_CHANNEL("E002", "잘못된 채널 ID"),
    INVALID_PHONE("E003", "잘못된 수신번호"),
    BLOCKED_USER("E004", "수신 차단 사용자"),
    SYSTEM_ERROR("E500", "시스템 오류"),
    TIMEOUT("E501", "처리 타임아웃"),
    RATE_LIMIT("E429", "요청 한도 초과");

    companion object {
        private val codeMap = entries.associateBy { it.code }

        fun fromCode(code: String): KakaoDirectErrorCode? = codeMap[code]
    }
}
