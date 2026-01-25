package com.messaging.platform.lgt.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * LG U+ 플랫폼 설정
 */
@ConfigurationProperties(prefix = "platform.lgt")
data class LgtProperties(
    val baseUrl: String = "https://api.lguplus.com",
    val apiKey: String = "",
    val timeout: Long = 10_000L,
    val enabled: Boolean = true
)
