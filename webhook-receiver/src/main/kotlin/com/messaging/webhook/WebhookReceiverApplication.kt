package com.messaging.webhook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebhookReceiverApplication

fun main(args: Array<String>) {
    runApplication<WebhookReceiverApplication>(*args)
}
