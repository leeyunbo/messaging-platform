package com.messaging.platform.kt.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * KT 플랫폼 설정
 */
@ConfigurationProperties(prefix = "platform.kt")
data class KtProperties(
    val baseUrl: String = "https://api.kt.com",
    val apiKey: String = "",
    val timeout: Long = 10_000L,
    val enabled: Boolean = true
)
