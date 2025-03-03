package com.reclaimyourattention.logic.phases

object BehavioralPhase: Phase() {
    // Atributos
    override val title: String = "Fase Conductual"
    override val description: String = ""
    override val weeks: List<Set<Task>> = listOf(
        BehavioralTasks.week1
    )
    override val storageKey = "BehavioralPhase"
}

object BehavioralTasks {
    val week1: Set<Task> = setOf(
        Task(
            id = "01",
            title = "¿Qué es la Terapía Conductual?",
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 3
        )
        // TODO("Faltan más")
    )
}