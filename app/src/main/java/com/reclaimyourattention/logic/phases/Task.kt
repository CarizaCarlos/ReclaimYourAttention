package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.services.ToolType

data class Task(
    val id: String,
    val title: String,
    val body: String,
    val tool: ToolType?,
    val taskPrerrequisitesID: Set<String>?,
    val isMandatory: Boolean,
    val readingMinutes: Int
)