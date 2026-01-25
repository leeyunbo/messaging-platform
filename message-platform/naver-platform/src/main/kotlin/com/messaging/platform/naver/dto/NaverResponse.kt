package com.messaging.platform.naver.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverResponse(
    @JsonProperty("statusCode")
    val statusCode: String,

    @JsonProperty("statusName")
    val statusName: String,

    @JsonProperty("requestId")
    val requestId: String? = null
) {
    fun isSuccess(): Boolean = statusCode == "202"
}
