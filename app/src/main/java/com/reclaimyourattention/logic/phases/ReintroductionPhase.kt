package com.reclaimyourattention.logic.phases

object ReintroductionPhase: Phase() {
    // Atributos
    override val title: String = "Fase de Reintroducción"
    override val description: String = ""
    override val weeks: List<Set<Task>> = listOf(
        ReintroductionTasks.week1
    )
    override val storageKey = "ReintroductionPhase"
}

object ReintroductionTasks {
    val week1: Set<Task> = setOf(
        Task( //TODO()
            id = "01",
            title = "¿Qué es la Terapía Conductual?",
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 3
        )
    )
    // TODO("Faltan más")
}