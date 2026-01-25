package com.messaging.bootstrap.alimtalk

import com.messaging.core.kakao.domain.AlimtalkRequest
import com.messaging.core.kakao.domain.ResponseMethod
import com.messaging.usecase.alimtalk.AlimtalkSendUseCase
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class AlimtalkMessageConsumer(
    private val alimtalkSendUseCase: AlimtalkSendUseCase
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun onMessage(message: AlimtalkMessage) {
        log.info("Received message: messageId={}", message.messageId)

        runBlocking {
            try {
                val request = AlimtalkRequest(
                    messageId = message.messageId,
                    responseMethod = ResponseMethod.valueOf(message.responseMethod),
                    timeout = message.timeout,
                    variables = message.variables
                )

                val result = alimtalkSendUseCase.send(request)
                log.info("Send completed: messageId={}, success={}", message.messageId, result.success)
            } catch (e: Exception) {
                log.error("Failed to process message: messageId={}, error={}",
                    message.messageId, e.message, e)
            }
        }
    }
}

data class AlimtalkMessage(
    val messageId: String,
    val responseMethod: String = "PUSH",
    val timeout: Long? = null,
    val variables: Map<String, Any?> = emptyMap()
)
