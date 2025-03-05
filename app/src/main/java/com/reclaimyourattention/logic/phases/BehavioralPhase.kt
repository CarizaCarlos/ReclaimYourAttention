package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.services.ToolType

object BehavioralPhase: Phase() {
    // Atributos
    override val title: String = "Fase Conductual"
    override val description: String = "En ésta fase se incrementarán las restricciones, nos enfocaremos en reemplazar los malos hábitos relacionados con el celular por hábitos más saludables, centrados en incrementar tu bienestar"
    override val weeks: List<Set<Task>> = listOf(
        BehavioralTasks.week1,
        BehavioralTasks.week2
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
        Task(
            id = "06",
            title = "Usa la Convenencia a tu Favor",
            tool = null,
            taskPrerrequisitesID = setOf("05"),
            isMandatory = true,
            readingMinutes = 3
        ),
        Task(
            id = "07",
            title = "Usa la Inconvenencia a tu Favor",
            tool = ToolType.WAIT_TIME,
            taskPrerrequisitesID = setOf("06"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "08",
            title = "Identifica y Enfrenta Detonantes",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = true,
            readingMinutes = 2
        ),
        Task(
            id = "09",
            title = "Protege tus Horarios de Sueño",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = true,
            readingMinutes = 2
        )
    )

    val week2: Set<Task> = setOf(
        Task(
            id = "10",
            title = "Revisa tu Progreso y Realiza Ajustes",
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 3
        )
    )
}