package com.messaging.core.kakao.domain

/**
 * 카카오 발송 결과
 */
data class KakaoSendResult(
    val success: Boolean,
    val resultCode: String? = null,
    val resultMessage: String? = null,
    val serialNumber: String? = null
) {
    companion object {
        fun success(serialNumber: String? = null) = KakaoSendResult(
            success = true,
            serialNumber = serialNumber
        )

        fun fail(code: String, message: String) = KakaoSendResult(
            success = false,
            resultCode = code,
            resultMessage = message
        )

        fun unKnownError() = KakaoSendResult(
            success = false,
            resultCode = "9999",
            resultMessage = "UNKNOWN_ERROR"
        )

        fun kakaoApiError() = KakaoSendResult(
            success = false,
            resultCode = "9998",
            resultMessage = "KAKAO_API_ERROR"
        )
    }
}
