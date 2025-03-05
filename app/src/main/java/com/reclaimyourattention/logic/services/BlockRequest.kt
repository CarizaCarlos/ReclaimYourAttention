package com.reclaimyourattention.logic.services

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BlockRequest (
    val message: String,
    val unblockTime: Instant?,
    val showCountdown: Boolean
)

@Serializable
enum class ToolType { WAIT_TIME, LIMIT_SESSION, LIMIT_DAILY, REST_REMINDERS, LIMIT_NOTIFICATIONS }

// Prioridad de las Herramientas
val toolTypePriority = listOf(
    ToolType.LIMIT_DAILY,
    ToolType.LIMIT_SESSION,
    ToolType.WAIT_TIME,
)