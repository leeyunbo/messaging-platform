package com.messaging.platform.lgt

import com.messaging.core.sms.domain.SmsSendResult
import com.messaging.platform.lgt.config.LgtApi
import com.messaging.platform.lgt.config.LgtProperties
import com.messaging.platform.lgt.dto.LgtResponse
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
class LgtApiClient(
    @param:Qualifier("lgtWebClient") private val webClient: WebClient,
    @param:Qualifier("lgtCircuitBreaker") private val circuitBreaker: CircuitBreaker,
    @param:Qualifier("lgtRetry") private val retry: Retry,
    private val config: LgtProperties
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
                .header(LgtApi.HEADER_API_KEY, config.apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LgtResponse::class.java)
                .timeout(Duration.ofMillis(config.timeout))
                .retryWhen(retry)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .awaitSingleOrNull()

            handleResponse(response, messageId, messageType)
        } catch (e: WebClientResponseException) {
            log.error("HTTP error from LGT API: messageId={}, status={}", messageId, e.statusCode)
            SmsSendResult.fail("HTTP_${e.statusCode.value()}", e.responseBodyAsString.ifBlank { "HTTP error" })
        } catch (e: Exception) {
            log.error("Failed to send via LGT: messageId={}, error={}", messageId, e.message)
            SmsSendResult.fail("EXCEPTION", e.message ?: "Unknown error")
        }
    }

    private fun handleResponse(
        response: LgtResponse?,
        messageId: String,
        messageType: String
    ): SmsSendResult {
        if (response == null) {
            return SmsSendResult.fail("EMPTY_RESPONSE", "Empty response from LGT")
        }

        if (response.isSuccess()) {
            log.info("{} sent successfully via LGT: messageId={}", messageType, messageId)
            return SmsSendResult.success(LgtApi.SUCCESS_CODE, LgtApi.SUCCESS_MESSAGE)
        }

        log.warn("LGT API returned error: messageId={}, code={}", messageId, response.errorCode)
        return SmsSendResult.fail(response.errorCode ?: "UNKNOWN", response.errorMessage ?: "Unknown error")
    }
}
