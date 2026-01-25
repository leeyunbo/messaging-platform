package com.messaging.platform.lgt.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LgtResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("error_code")
    val errorCode: String? = null,

    @JsonProperty("error_message")
    val errorMessage: String? = null,

    @JsonProperty("transaction_id")
    val transactionId: String? = null
) {
    fun isSuccess(): Boolean = status == "success"
}
