package com.reclaimyourattention.logic

class FirstPhase: Phase() {
    // Variables Superclase
    override var title: String = "Fase de Concientización"
    override var description: String =
        ""
    override var tasks: MutableSet<Task> = mutableSetOf(
        Task(
            "¿Cómo funciona la App?",
            "...",
            2,
        )
        // Faltan más
    )

    // Métodos Superclase
    override fun areRequirementsMet(): Boolean {
        // Falta implementar
        return false
    }
}