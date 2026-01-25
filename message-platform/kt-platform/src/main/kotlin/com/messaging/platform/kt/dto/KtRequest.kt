package com.messaging.platform.kt.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KtSmsRequest(
    @JsonProperty("msgId")
    val msgId: String,

    @JsonProperty("receiver")
    val receiver: String,

    @JsonProperty("sender")
    val sender: String,

    @JsonProperty("message")
    val message: String
)
