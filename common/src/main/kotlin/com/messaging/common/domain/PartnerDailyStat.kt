package com.messaging.common.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 파트너별 일별 발송 통계
 */
@Entity
@Table(
    name = "partner_daily_stats",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_stat_partner_date_type",
            columnNames = ["partnerId", "statDate", "messageType"]
        )
    ],
    indexes = [
        Index(name = "idx_stat_partner_id", columnList = "partnerId"),
        Index(name = "idx_stat_date", columnList = "statDate")
    ]
)
class PartnerDailyStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 50)
    val partnerId: String,

    @Column(nullable = false)
    val statDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val messageType: MessageType,

    @Column(nullable = false)
    var totalCount: Long = 0,

    @Column(nullable = false)
    var successCount: Long = 0,

    @Column(nullable = false)
    var failCount: Long = 0,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun incrementTotal() {
        totalCount++
    }

    fun incrementSuccess() {
        successCount++
    }

    fun incrementFail() {
        failCount++
    }
}
