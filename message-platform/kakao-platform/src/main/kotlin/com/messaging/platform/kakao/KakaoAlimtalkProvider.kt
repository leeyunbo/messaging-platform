package com.messaging.platform.kakao

import com.messaging.core.kakao.domain.AlimtalkProvider
import com.messaging.core.kakao.domain.AlimtalkRequest
import com.messaging.core.kakao.domain.KakaoSendResult
import com.messaging.library.idgen.MessageIdGenerator
import com.messaging.platform.kakao.config.KakaoProperties
import org.springframework.stereotype.Component

/**
 * 카카오 알림톡 Provider 구현체
 */
@Component
class KakaoAlimtalkProvider(
    private val apiClient: KakaoAlimtalkApiClient,
    private val config: KakaoProperties
) : AlimtalkProvider {

    override suspend fun send(request: AlimtalkRequest): KakaoSendResult {
        if (!config.enabled) {
            return KakaoSendResult.unKnownError()
        }

        val serialNumber = MessageIdGenerator.generate()
        return apiClient.send(request.toApiBody(serialNumber), request.messageId)
    }

    private fun AlimtalkRequest.toApiBody(serialNumber: String) = buildMap {
        putAll(variables)
        put("serialNumber", serialNumber)
        put("responseMethod", responseMethod.name)
        timeout?.let { put("timeout", it) }
    }
}
