package com.messaging.platform.kakaodirect

import com.messaging.core.kakao.domain.KakaoSendResult
import com.messaging.platform.kakaodirect.config.KakaoDirectApi
import com.messaging.platform.kakaodirect.config.KakaoDirectProperties
import com.messaging.platform.kakaodirect.dto.KakaoDirectResponse
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.util.retry.Retry
import java.time.Duration

@Component
class KakaoDirectApiClient(
    @param:Qualifier("kakaoDirectWebClient") private val webClient: WebClient,
    @param:Qualifier("kakaoDirectCircuitBreaker") private val circuitBreaker: CircuitBreaker,
    @param:Qualifier("kakaoDirectRetry") private val retry: Retry,
    private val config: KakaoDirectProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun send(
        path: String,
        body: Map<String, Any?>,
        messageId: String,
        serialNumber: String
    ): KakaoSendResult {
        return try {
            log.debug("Sending BrandMessage: messageId={}, serialNumber={}, path={}", messageId, serialNumber, path)

            val response = webClient.post()
                .uri("${config.baseUrl}$path")
                .contentType(MediaType.APPLICATION_JSON)
                .header(KakaoDirectApi.HEADER_API_KEY, config.apiKey)
                .header(KakaoDirectApi.HEADER_SECRET_KEY, config.secretKey)
                .header(KakaoDirectApi.HEADER_SERIAL_NUMBER, serialNumber)
                .bodyValue(body)
                .retrieve()
                .bodyToMono<KakaoDirectResponse>()
                .timeout(Duration.ofMillis(config.timeout))
                .retryWhen(retry)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .awaitSingleOrNull()

            handleResponse(response, messageId)
        } catch (e: WebClientResponseException) {
            log.error("HTTP error from Kakao Direct API: messageId={}, status={}", messageId, e.statusCode)
            KakaoSendResult.fail("HTTP_${e.statusCode.value()}", e.responseBodyAsString.ifBlank { "HTTP error" })
        } catch (e: Exception) {
            log.error("Failed to send BrandMessage: messageId={}, error={}", messageId, e.message)
            KakaoSendResult.fail("EXCEPTION", e.message ?: "Unknown error")
        }
    }

    private fun handleResponse(response: KakaoDirectResponse?, messageId: String): KakaoSendResult {
        if (response == null) {
            return KakaoSendResult.kakaoApiError()
        }

        if (response.isSuccess()) {
            return KakaoSendResult.success()
        }

        return response.error?.let { error ->
            KakaoSendResult.fail(error.code, error.message)
        } ?: return KakaoSendResult.unKnownError()
    }
}
