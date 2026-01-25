package com.messaging.platform.kakaodirect.dto

/**
 * 카카오 다이렉트 API 응답
 */
data class KakaoDirectResponse(
    val status: String,
    val error: KakaoDirectError? = null,
    val result: String,
) {
    fun isSuccess(): Boolean = status == "200"
}

data class KakaoDirectError(
    val code: String,
    val message: String
)