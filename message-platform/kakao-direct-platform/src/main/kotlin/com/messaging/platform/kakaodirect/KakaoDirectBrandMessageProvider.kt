package com.messaging.platform.kakaodirect

import com.messaging.core.kakao.domain.BrandMessageProvider
import com.messaging.core.kakao.domain.BrandMessageRequest
import com.messaging.core.kakao.domain.KakaoSendResult
import com.messaging.library.idgen.MessageIdGenerator
import com.messaging.platform.kakaodirect.config.KakaoDirectProperties
import org.springframework.stereotype.Component

/**
 * 카카오 다이렉트 브랜드메시지 Provider
 */
@Component
class KakaoDirectBrandMessageProvider(
    private val apiClient: KakaoDirectApiClient,
    private val config: KakaoDirectProperties
) : BrandMessageProvider {

    override suspend fun send(request: BrandMessageRequest): KakaoSendResult {
        if (!config.enabled) {
            return KakaoSendResult.unKnownError()
        }

        val serialNumber = MessageIdGenerator.generate()
        val path = request.brandMessageType.path

        return apiClient.send(
            path = path,
            body = request.toApiBody(),
            messageId = request.messageId,
            serialNumber
        )
    }

    private fun BrandMessageRequest.toApiBody() = buildMap {
        putAll(variables)
        put("targeting", targeting.name)
    }
}
