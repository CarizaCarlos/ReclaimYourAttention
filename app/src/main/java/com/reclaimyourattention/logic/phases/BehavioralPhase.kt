package com.reclaimyourattention.logic.phases

object BehavioralPhase: Phase() {
    // Atributos
    override val title: String = "Fase Conductual"
    override val description: String = "En ésta fase se incrementarán las restricciones, nos enfocaremos en reemplazar los malos hábitos relacionados con el celular por hábitos más saludables, centrados en incrementar tu bienestar"
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
        ),
        Task(
            id = "02",
            title = "La Importancia de la Actividad Física",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = false,
            readingMinutes = 2
        ),
        Task(
            id = "03",
            title = "Journaling y la Autoconciencia",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = false,
            readingMinutes = 2
        ),
        Task(
            id = "04",
            title = "Apóyate de Tus Cercanos",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = false,
            readingMinutes = 2
        ),
        Task(
            id = "05",
            title = "Identifica las Apps Problemáticas",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = true,
            readingMinutes = 3
        ),
        // TODO("Faltan más")
    )
}