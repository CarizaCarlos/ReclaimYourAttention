package com.reclaimyourattention.logic.phases

class ReintroductionPhase: Phase() {
    // Atributos
    override val title: String
        get() = "Fase de Reintroducción"
    override val description: String
        get() = TODO()
    override val weeks: List<Set<Task>> = listOf(
        ReintroductionTasks.week1
    )
}

object ReintroductionTasks {
    val week1: Set<Task> = setOf(
        Task(
            id = TODO(),
            title = TODO(),
            body = TODO(),
            tool = TODO(),
            taskPrerrequisitesID = TODO(),
            isMandatory = TODO(),
            readingMinutes = TODO()
        )
    )
    // TODO("Faltan más")
}