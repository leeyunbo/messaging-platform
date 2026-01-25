package com.messaging.platform.naver

import com.messaging.core.naver.domain.NaverSendResult
import com.messaging.core.naver.domain.NaverSmsProvider
import com.messaging.core.naver.domain.NaverSmsRequest
import com.messaging.core.naver.domain.NaverSmsType
import com.messaging.platform.naver.config.NaverApi
import org.springframework.stereotype.Component

@Component
class NaverSmsProviderImpl(
    private val naverApiClient: NaverApiClient
) : NaverSmsProvider {

    override suspend fun send(request: NaverSmsRequest): NaverSendResult {
        if (!naverApiClient.isEnabled) {
            return NaverSendResult.fail("DISABLED", "Naver provider is disabled")
        }

        val msgType = when (request.type) {
            NaverSmsType.MMS -> "MMS"
            NaverSmsType.LMS -> "LMS"
            else -> "SMS"
        }

        val body = mapOf(
            "type" to msgType,
            "contentType" to "COMM",
            "countryCode" to "82",
            "from" to request.callback,
            "content" to request.content,
            "messages" to listOf(
                buildMap {
                    put("to", request.recipient)
                    request.subject?.let { put("subject", it) }
                }
            )
        )

        val path = NaverApi.SMS_SEND_PATH_TEMPLATE.format(naverApiClient.serviceId)
        return naverApiClient.send(
            path = path,
            request = body,
            messageId = request.messageId,
            messageType = "SMS"
        )
    }
}
