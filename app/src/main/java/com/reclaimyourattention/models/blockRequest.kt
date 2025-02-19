package com.reclaimyourattention.models

import kotlinx.serialization.Serializable

@Serializable
data class AppBlockRequest(
    val tool: ToolType,
    val message: String,
    val duration: Int,
    val showCountdown: Boolean
)

@Serializable
enum class ToolType {WAIT_TIME, SCHEDULED_BLOCK, LIMIT_DAILY, LIMIT_SESSION, INCREMENTAL_PAUSE}