package com.reclaimyourattention.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class BlockRequest(
    val tool: ToolType,
    val message: String,
    val unblockDateTime: LocalDateTime,
    val showCountdown: Boolean
)

@Serializable
enum class ToolType {WAIT_TIME, SCHEDULED_BLOCK, LIMIT_DAILY, LIMIT_SESSION, INCREMENTAL_PAUSE, INDIFINITELY}