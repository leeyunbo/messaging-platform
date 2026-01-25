package com.messaging.bootstrap.brandmessage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.messaging"])
@ConfigurationPropertiesScan(basePackages = ["com.messaging"])
class BrandMessageSenderApplication

fun main(args: Array<String>) {
    runApplication<BrandMessageSenderApplication>(*args)
}
