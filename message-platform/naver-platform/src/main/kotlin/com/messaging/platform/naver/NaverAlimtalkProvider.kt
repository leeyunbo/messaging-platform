package com.messaging.platform.naver

import com.messaging.core.naver.domain.NaverAlimtalkProvider
import com.messaging.core.naver.domain.NaverAlimtalkRequest
import com.messaging.core.naver.domain.NaverSendResult
import com.messaging.platform.naver.config.NaverApi
import org.springframework.stereotype.Component

@Component
class NaverAlimtalkProviderImpl(
    private val naverApiClient: NaverApiClient
) : NaverAlimtalkProvider {

    override suspend fun send(request: NaverAlimtalkRequest): NaverSendResult {
        if (!naverApiClient.isEnabled) {
            return NaverSendResult.fail("DISABLED", "Naver provider is disabled")
        }

        val buttons = request.buttons.map { btn ->
            buildMap {
                put("type", btn.type.name)
                put("name", btn.name)
                btn.linkMobile?.let { put("linkMobile", it) }
                btn.linkPc?.let { put("linkPc", it) }
            }
        }.ifEmpty { null }

        val body = mapOf(
            "templateCode" to request.templateCode,
            "plusFriendId" to request.plusFriendId,
            "messages" to listOf(
                buildMap {
                    put("to", request.recipient)
                    put("content", request.content)
                    buttons?.let { put("buttons", it) }
                }
            )
        )

        val path = NaverApi.ALIMTALK_SEND_PATH_TEMPLATE.format(naverApiClient.serviceId)
        return naverApiClient.send(
            path = path,
            request = body,
            messageId = request.messageId,
            messageType = "Alimtalk"
        )
    }
}
