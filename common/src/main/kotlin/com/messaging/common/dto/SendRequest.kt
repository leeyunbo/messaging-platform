package com.messaging.common.dto

import com.messaging.common.domain.MessageType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * 클라이언트 → Receiver 발송 요청
 */
data class SendRequest(
    @field:NotNull(message = "발송 타입은 필수입니다")
    val type: MessageType,

    @field:NotBlank(message = "메시지 내용은 필수입니다")
    val content: String,
    val clientMsgId: String? = null,
    val detail: Map<String, Any?> = emptyMap()
)

/**
 * Receiver → Client 발송 응답
 */
data class SendResponse(
    val messageId: String?,
    val code: String,
    val message: String
) {
    companion object {
        fun success(messageId: String) = SendResponse(
            messageId = messageId,
            code = ResponseCode.SUCCESS.code,
            message = ResponseCode.SUCCESS.message
        )

        fun error(responseCode: ResponseCode) = SendResponse(
            messageId = null,
            code = responseCode.code,
            message = responseCode.message
        )

        fun error(code: String, message: String) = SendResponse(
            messageId = null,
            code = code,
            message = message
        )
    }
}
