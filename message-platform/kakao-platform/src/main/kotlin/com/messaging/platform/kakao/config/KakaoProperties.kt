package com.messaging.platform.kakao.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "platform.kakao")
data class KakaoProperties(
    val baseUrl: String = "https://api.kakao.com",
    val apiKey: String = "",
    val timeout: Long = 10_000L,
    val enabled: Boolean = true
)
