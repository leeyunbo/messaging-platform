package com.messaging.common.dto

import java.time.LocalDateTime

/**
 * Reporter → Client Webhook 페이로드
 */
data class WebhookPayload(
    val messageId: String,
    val status: String,
    val resultCode: String? = null,
    val resultMessage: String? = null,
    val sentAt: LocalDateTime? = null,
    val deliveredAt: LocalDateTime? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

/**
 * 클라이언트가 조회할 수 있는 메시지 상태 응답
 */
data class MessageStatusResponse(
    val messageId: String,
    val type: String,
    val status: String,
    val content: String,
    val detail: Map<String, Any?>,
    val resultCode: String? = null,
    val resultMessage: String? = null,
    val createdAt: LocalDateTime,
    val sentAt: LocalDateTime? = null,
    val deliveredAt: LocalDateTime? = null
)
