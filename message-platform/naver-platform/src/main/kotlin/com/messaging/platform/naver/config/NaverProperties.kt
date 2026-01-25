package com.messaging.platform.naver.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 네이버 플랫폼 설정
 */
@ConfigurationProperties(prefix = "platform.naver")
data class NaverProperties(
    val baseUrl: String = "https://sens.apigw.ntruss.com",
    val accessKey: String = "",
    val secretKey: String = "",
    val serviceId: String = "",
    val timeout: Long = 10_000L,
    val enabled: Boolean = true
)
