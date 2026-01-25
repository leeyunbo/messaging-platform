package com.messaging.usecase.brandmessage

import com.messaging.core.kakao.domain.BrandMessageProvider
import com.messaging.core.kakao.domain.BrandMessageRequest
import com.messaging.core.kakao.domain.KakaoReportCode
import com.messaging.core.kakao.domain.KakaoSendResult
import com.messaging.core.report.domain.Report
import com.messaging.core.report.domain.ReportPublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * 카카오 브랜드메시지 발송 유스케이스
 */
@Service
class BrandMessageSendUseCase(
    private val brandMessageProvider: BrandMessageProvider,
    private val reportPublisher: ReportPublisher
) {
    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun send(request: BrandMessageRequest): KakaoSendResult {
        log.info("Processing BrandMessage send: messageId={}, type={}", request.messageId, request.brandMessageType)

        val result = brandMessageProvider.send(request)
        val report = Report(
            messageId = request.messageId,
            code = KakaoReportCode.from(result)
        )
        reportPublisher.publish(report)
        log.info("Report published: messageId={}, code={}", request.messageId, report.code)

        return result
    }
}
