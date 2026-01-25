package com.messaging.platform.kakao.dto

/**
 * 카카오 알림톡 API 응답
 */
data class KakaoResponse(
    val code: String,
    val message: String,
    val receivedAt: String,
) {
    fun isSuccess(): Boolean = code == "0000"
}
