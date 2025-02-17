package com.reclaimyourattention.logic

class Task(
    private var title: String,
    private var description: String,
    private var duration: Int,
    private var tools: MutableSet<Tool>,
    private var done: Boolean = false
) {}