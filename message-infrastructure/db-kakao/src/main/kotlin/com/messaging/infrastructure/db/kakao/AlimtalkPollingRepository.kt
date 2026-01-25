package com.messaging.infrastructure.db.kakao

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AlimtalkPollingRepository : CoroutineCrudRepository<AlimtalkPollingEntity, Long>
