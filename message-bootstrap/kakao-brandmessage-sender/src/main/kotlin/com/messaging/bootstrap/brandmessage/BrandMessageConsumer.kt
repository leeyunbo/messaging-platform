package com.messaging.bootstrap.brandmessage

import com.messaging.core.kakao.domain.BrandMessageRequest
import com.messaging.core.kakao.domain.BrandMessageType
import com.messaging.core.kakao.domain.Targeting
import com.messaging.usecase.brandmessage.BrandMessageSendUseCase
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class BrandMessageConsumer(
    private val brandMessageSendUseCase: BrandMessageSendUseCase
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun onMessage(message: BrandMessage) {
        log.info("Received message: messageId={}, type={}", message.messageId, message.brandMessageType)

        runBlocking {
            try {
                val request = BrandMessageRequest(
                    messageId = message.messageId,
                    brandMessageType = BrandMessageType.valueOf(message.brandMessageType),
                    targeting = Targeting.valueOf(message.targeting),
                    variables = message.variables
                )

                val result = brandMessageSendUseCase.send(request)
                log.info("Send completed: messageId={}, success={}", message.messageId, result.success)
            } catch (e: Exception) {
                log.error("Failed to process message: messageId={}, error={}",
                    message.messageId, e.message, e)
            }
        }
    }
}

data class BrandMessage(
    val messageId: String,
    val brandMessageType: String = "BASIC",
    val targeting: String = "I",
    val variables: Map<String, Any?> = emptyMap()
)
