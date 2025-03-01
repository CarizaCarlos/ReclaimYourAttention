package com.reclaimyourattention.logic.phases

class BehavioralPhase: Phase() {
    // Atributos
    override val title: String
        get() = "Fase Conductual"
    override val description: String
        get() = TODO()
    override val weeks: List<Set<Task>> = listOf(
        BehavioralTasks.week1
    )
}

object BehavioralTasks {
    val week1: Set<Task> = setOf(
        Task(
            id = "01",
            title = "¿Qué es la Terapía Conductual?",
            body = TODO(),
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 3
        )
        // TODO("Faltan más")
    )
}