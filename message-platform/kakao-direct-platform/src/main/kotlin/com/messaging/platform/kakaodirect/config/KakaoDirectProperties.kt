package com.messaging.platform.kakaodirect.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "platform.kakao-direct")
data class KakaoDirectProperties(
    val baseUrl: String = "https://api.kakaodirect.com",
    val apiKey: String = "",
    val secretKey: String = "",
    val timeout: Long = 10_000L,
    val enabled: Boolean = true
)
