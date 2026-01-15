package com.messaging.common.domain

enum class MessageType(
    val maxLength: Int,
    val description: String,
    val maxLengthType: MaxLengthType
) {
    SMS(90, "단문 문자", MaxLengthType.BYTE),
    LMS(4000, "장문 문자", MaxLengthType.BYTE),
    MMS(2000, "멀티미디어 문자", MaxLengthType.BYTE),
    KAKAO_ALIMTALK(1000, "카카오 알림톡", MaxLengthType.CHARACTER),
    KAKAO_BRAND_MESSAGE(1400, "카카오 브랜드메시지", MaxLengthType.CHARACTER),
    NAVER_TALK(1000, "네이버 톡톡", MaxLengthType.CHARACTER),
    RCS(1300, "RCS", MaxLengthType.BYTE);

    fun validate(content: String): Boolean {
        return content.length <= maxLength
    }

    enum class MaxLengthType {
        BYTE, CHARACTER
    }

}
