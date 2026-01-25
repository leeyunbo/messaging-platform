package com.messaging.platform.kt.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KtResponse(
    @JsonProperty("code")
    val code: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: KtResponseData? = null
) {
    fun isSuccess(): Boolean = code == "200"
}

data class KtResponseData(
    @JsonProperty("msgId")
    val msgId: String? = null
)
