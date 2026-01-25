package com.messaging.platform.lgt.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LgtRequest(
    @JsonProperty("transaction_id")
    val transactionId: String,

    @JsonProperty("to")
    val to: String,

    @JsonProperty("from")
    val from: String,

    @JsonProperty("text")
    val text: String,

    @JsonProperty("type")
    val type: String = "SMS"
)
