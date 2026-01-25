package com.messaging.platform.kakao

import com.messaging.core.kakao.domain.KakaoSendResult
import com.messaging.platform.kakao.config.KakaoAlimtalkApi
import com.messaging.platform.kakao.config.KakaoProperties
import com.messaging.platform.kakao.dto.KakaoResponse
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
class KakaoAlimtalkApiClient(
    @param:Qualifier("kakaoWebClient") private val webClient: WebClient,
    @param:Qualifier("kakaoCircuitBreaker") private val circuitBreaker: CircuitBreaker,
    @param:Qualifier("kakaoRetry") private val retry: Retry,
    private val config: KakaoProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun send(
        body: Map<String, Any?>,
        messageId: String
    ): KakaoSendResult {
        return try {
            val response = webClient.post()
                .uri("${config.baseUrl}${KakaoAlimtalkApi.SEND_PATH}")
                .contentType(MediaType.APPLICATION_JSON)
                .header(KakaoAlimtalkApi.HEADER_API_KEY, config.apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono<KakaoResponse>()
                .timeout(Duration.ofMillis(config.timeout))
                .retryWhen(retry)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .awaitSingleOrNull()

            handleResponse(response, messageId)
        } catch (e: WebClientResponseException) {
            log.error("HTTP error from Kakao Alimtalk API: messageId={}, status={}", messageId, e.statusCode)
            KakaoSendResult.kakaoApiError()
        } catch (e: Exception) {
            log.error("Failed to send Alimtalk: messageId={}, error={}", messageId, e.message)
            KakaoSendResult.kakaoApiError()
        }
    }

    private fun handleResponse(response: KakaoResponse?, messageId: String): KakaoSendResult {
        if (response == null) {
            return KakaoSendResult.kakaoApiError()
        }

        if (response.isSuccess()) {
            return KakaoSendResult.success()
        }

        return KakaoSendResult.fail(response.code, response.message)
    }
}
