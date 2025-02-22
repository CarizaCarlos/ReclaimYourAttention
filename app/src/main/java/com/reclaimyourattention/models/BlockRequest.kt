package com.reclaimyourattention.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BlockRequest(
    val message: String,
    val unblockTime: Instant?,
    val showCountdown: Boolean
)

@Serializable
enum class ToolType {WAIT_TIME, SCHEDULED_BLOCK, LIMIT_DAILY, LIMIT_SESSION, INCREMENTAL_PAUSE, INDEFINITELY}

// Prioridad de las Herramientas
val toolTypePriority = listOf(
    ToolType.INDEFINITELY,
    ToolType.WAIT_TIME
    // Ir agregando
)