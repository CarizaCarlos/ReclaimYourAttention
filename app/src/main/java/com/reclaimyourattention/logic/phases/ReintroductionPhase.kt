package com.reclaimyourattention.logic.phases

object ReintroductionPhase: Phase() {
    // Atributos
    override val title: String = "Fase de Reintroducción"
    override val description: String = ""
    override val weeks: List<Set<Task>> = listOf(
        ReintroductionTasks.week1
    )
}

object ReintroductionTasks {
    val week1: Set<Task> = setOf(
        // TODO()
    )
    // TODO("Faltan más")
}