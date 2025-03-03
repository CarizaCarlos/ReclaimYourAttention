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
enum class ToolType {WAIT_TIME, SCHEDULED_BLOCK, LIMIT_DAILY, LIMIT_SESSION, INDEFINITELY, DIAGNOSTIC, REST_REMINDERS }

// Prioridad de las Herramientas
val toolTypePriority = listOf(
    ToolType.INDEFINITELY,
    ToolType.LIMIT_DAILY,
    ToolType.SCHEDULED_BLOCK,
    ToolType.LIMIT_SESSION,
    ToolType.WAIT_TIME,
    // TODO("Incremental pause creo khelo dejamos como subherramienta de WaitTime")
)