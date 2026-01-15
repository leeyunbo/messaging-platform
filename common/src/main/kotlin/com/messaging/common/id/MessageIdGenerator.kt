package com.messaging.common.id

import io.hypersistence.tsid.TSID

/**
 * TSID 기반 메시지 ID 생성기
 *
 * - 시간순 정렬 가능
 * - 분산 환경에서 충돌 없음
 * - 13자리 문자열 (예: 0HJKXS3JP5W2T)
 */
object MessageIdGenerator {

    private val factory: TSID.Factory = TSID.Factory.builder()
        .withNodeBits(10)
        .build()

    fun generate(): String {
        return factory.generate().toString()
    }
}
