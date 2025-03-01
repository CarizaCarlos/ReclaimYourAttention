package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.services.ToolType

object AwarenessPhase: Phase() {
    // Atributos Superclase
    override val title: String = "Fase de Concientización"
    override val description: String = ""
    override val weeks: List<Set<Task>> = listOf(
        AwarenessTasks.week1
    )
    override val storageKey = "AwarenessPhase"
}

object AwarenessTasks {
    val week1: Set<Task> = setOf(
        Task(
            id = "01",
            title = "¿Cómo Funciona la App?",
            body = "", // TODO()
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 2
        ),
        Task(
            id = "02",
            title = "Autodiagnóstico",
            body = "", // TODO()
            tool = ToolType.DIAGNOSTIC,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "03",
            title = "Agrega tus Primeros Límites de Tiempo",
            body = "", // TODO()
            tool = ToolType.LIMIT_DAILY,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "03",
            title = "Agrega tus Primeros Límites de Tiempo",
            body = "", // TODO()
            tool = ToolType.LIMIT_DAILY,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 1
        )
        // TODO("Faltan más")
    )
}