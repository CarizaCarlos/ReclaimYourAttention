package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.services.ToolType

object AwarenessPhase: Phase() {
    // Atributos Superclase
    override val title: String = "Fase de Concientización"
    override val description: String = "En ésta fase aplicarás tus primeras restricciones, aprenderás a usar la app y recibirás información valiosa para tu progreso"
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
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 2
        ),
        Task(
            id = "02",
            title = "Autodiagnóstico",
            tool = null,
            taskPrerrequisitesID = setOf("01"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "03",
            title = "Agrega tus Primeros Límites de Tiempo",
            tool = ToolType.LIMIT_DAILY,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "04",
            title = "¿Cuánto Tiempo te Roba tu Teléfono?",
            tool = null,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "05",
            title = "Beneficios de la Escala de Grices y Cómo Activarla",
            tool = null,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 4
        ),
        Task(
            id = "06",
            title = "Controla las Notificaciones",
            tool = ToolType.LIMIT_NOTIFICATIONS,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 1
        ),
        Task(
            id = "07",
            title = "Añade Recordatorios para Descansar del Teléfono",
            tool = ToolType.REST_REMINDERS,
            taskPrerrequisitesID = setOf("02"),
            isMandatory = true,
            readingMinutes = 1
        )
    )
}