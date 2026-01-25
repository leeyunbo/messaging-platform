package com.messaging.platform.skt.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * SKT 플랫폼 설정
 */
@ConfigurationProperties(prefix = "platform.skt")
data class SktProperties(
    val baseUrl: String = "https://api.skt.com",
    val apiKey: String = "",
    val timeout: Long = 10_000L,
    val enabled: Boolean = true
)
