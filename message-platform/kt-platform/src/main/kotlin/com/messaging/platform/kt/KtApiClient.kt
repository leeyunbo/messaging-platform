package com.messaging.platform.kt

import com.messaging.core.sms.domain.SmsSendResult
import com.messaging.platform.kt.config.KtApi
import com.messaging.platform.kt.config.KtProperties
import com.messaging.platform.kt.dto.KtResponse
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.util.retry.Retry
import java.time.Duration

@Component
class KtApiClient(
    @param:Qualifier("ktWebClient") private val webClient: WebClient,
    @param:Qualifier("ktCircuitBreaker") private val circuitBreaker: CircuitBreaker,
    @param:Qualifier("ktRetry") private val retry: Retry,
    private val config: KtProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun <T : Any> send(
        path: String,
        request: T,
        messageId: String,
        messageType: String
    ): SmsSendResult {
        return try {
            val response = webClient.post()
                .uri("${config.baseUrl}$path")
                .contentType(MediaType.APPLICATION_JSON)
                .header(KtApi.HEADER_AUTHORIZATION, "${KtApi.BEARER_PREFIX}${config.apiKey}")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KtResponse::class.java)
                .timeout(Duration.ofMillis(config.timeout))
                .retryWhen(retry)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .awaitSingleOrNull()

            handleResponse(response, messageId, messageType)
        } catch (e: WebClientResponseException) {
            log.error("HTTP error from KT API: messageId={}, status={}", messageId, e.statusCode)
            SmsSendResult.fail("HTTP_${e.statusCode.value()}", e.responseBodyAsString.ifBlank { "HTTP error" })
        } catch (e: Exception) {
            log.error("Failed to send via KT: messageId={}, error={}", messageId, e.message)
            SmsSendResult.fail("EXCEPTION", e.message ?: "Unknown error")
        }
    }

    private fun handleResponse(
        response: KtResponse?,
        messageId: String,
        messageType: String
    ): SmsSendResult {
        if (response == null) {
            return SmsSendResult.fail("EMPTY_RESPONSE", "Empty response from KT")
        }

        if (response.isSuccess()) {
            log.info("{} sent successfully via KT: messageId={}", messageType, messageId)
            return SmsSendResult.success(response.code, response.message)
        }

        log.warn("KT API returned error: messageId={}, code={}", messageId, response.code)
        return SmsSendResult.fail(response.code, response.message)
    }
}
