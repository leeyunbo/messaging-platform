package com.messaging.platform.kt.config

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.netty.channel.ConnectTimeoutException
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.net.ConnectException
import java.time.Duration

@Configuration
@EnableConfigurationProperties(KtProperties::class)
class KtBeanConfig {

    @Bean
    fun ktWebClient(): WebClient = WebClient.builder()
        .codecs { it.defaultCodecs().maxInMemorySize(1024 * 1024) }
        .build()

    @Bean
    fun ktCircuitBreaker(): CircuitBreaker = CircuitBreaker.of(
        "kt",
        CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .slowCallRateThreshold(80f)
            .slowCallDurationThreshold(Duration.ofSeconds(5))
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(5)
            .slidingWindowSize(10)
            .build()
    )

    @Bean
    fun ktRetry(): Retry = Retry.backoff(3, Duration.ofMillis(100))
        .filter { e -> e is ConnectTimeoutException || e is ConnectException }
}
