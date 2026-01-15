package com.messaging.common.dto

import com.messaging.common.domain.MessageStatus
import com.messaging.common.domain.MessageType
import java.time.LocalDateTime

/**
 * Provider → Sender 발송 결과
 */
data class SendResult(
    val success: Boolean,
    val providerMessageId: String? = null,
    val resultCode: String,
    val resultMessage: String
)

/**
 * RabbitMQ를 통해 전달되는 발송 요청 이벤트
 */
data class MessageSendEvent(
    val messageId: String,
    val partnerId: String,
    val type: MessageType,
    val content: String,
    val detail: Map<String, Any?> = emptyMap(),
    val createdAt: LocalDateTime = LocalDateTime.now()
)

/**
 * RabbitMQ를 통해 전달되는 발송 결과 리포트 이벤트
 */
data class MessageReportEvent(
    val messageId: String,
    val partnerId: String,
    val status: MessageStatus,
    val resultCode: String? = null,
    val resultMessage: String? = null,
    val providerMessageId: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
