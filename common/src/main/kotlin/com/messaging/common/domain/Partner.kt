package com.messaging.common.domain

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 메시징 서비스를 사용하는 파트너 (고객사)
 */
@Entity
@Table(name = "partners")
class Partner(
    @Id
    @Column(length = 50)
    val partnerId: String,

    @Column(nullable = false, length = 100)
    val partnerName: String,

    // 인증용 (API Key → JWT 발급)
    @Column(nullable = false, length = 100, unique = true)
    val apiKey: String,

    @Column(nullable = false, length = 255)
    val apiSecret: String,

    // Webhook 설정
    @Column(nullable = false, length = 500)
    val webhookUrl: String,

    @Column(nullable = false, length = 255)
    val webhookSecret: String,  // HMAC 서명용

    // Rate Limit (초당 요청 수)
    @Column(nullable = false)
    val rateLimitPerSecond: Int = 100,

    @Column(nullable = false)
    val active: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
