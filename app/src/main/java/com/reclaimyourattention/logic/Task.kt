package com.reclaimyourattention.logic

import com.reclaimyourattention.logic.tools.Tool

class Task(
    private var title: String,
    private var content: String,
    private var durationMinutes: Int,
    private var tools: MutableSet<Tool> = mutableSetOf(),
    private var done: Boolean = false
) {}