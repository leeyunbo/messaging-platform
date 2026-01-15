package com.messaging.reporter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReporterApplication

fun main(args: Array<String>) {
    runApplication<ReporterApplication>(*args)
}
