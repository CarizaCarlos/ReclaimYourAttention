package com.reclaimyourattention.logic.phases

object PhaseManager {
    // Atributos
    private val phases: List<Phase> = listOf(
        AwarenessPhase,
        BehavioralPhase,
        ReintroductionPhase
    )
    private var currentPhaseIndex: Int = 0

    // Métodos
    fun getCurrentPhase(): Phase? = phases.getOrNull(currentPhaseIndex)

    fun canAdvancePhase(): Boolean = getCurrentPhase()?.areRequirementsMet() ?: false

    fun advancePhase(): Boolean {
        if (canAdvancePhase() && currentPhaseIndex < phases.size-1) {
            currentPhaseIndex++
            return true
            // TODO("Hacer algo especial si ya no se puede avanzar más, lo de la fase de monitoreao")
        }
        return false
    }
}