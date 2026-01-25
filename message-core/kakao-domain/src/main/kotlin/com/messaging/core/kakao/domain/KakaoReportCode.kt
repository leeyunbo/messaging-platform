package com.messaging.core.kakao.domain

/**
 * 카카오 리포트 코드 매핑
 */
object KakaoReportCode {
    const val SUCCESS = "7000"
    const val UNKNOWN_ERROR = "7999"

    private val codeMap = mapOf(
        "9998" to "7101",  // KAKAO_API_ERROR
        "3320" to "7200",  // INVALID_PARAMETER
        "3201" to "7201",  // INVALID_CHANNEL
        "3202" to "7202",  // INVALID_PHONE
        "3203" to "7203",  // BLOCKED_USER
        "9300" to "7300",  // SYSTEM_ERROR
        "9501" to "7301",  // TIMEOUT
        "9999" to "7302"   // RATE_LIMIT
    )

    fun from(result: KakaoSendResult): String {
        if (result.success) return SUCCESS
        return result.resultCode?.let { codeMap[it] } ?: UNKNOWN_ERROR
    }
}
