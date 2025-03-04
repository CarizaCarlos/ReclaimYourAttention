package com.reclaimyourattention.logic.phases

import android.util.Log
import com.reclaimyourattention.logic.StorageManager

object PhaseManager {
    // Atributos
    val phases: List<Phase> = listOf(
        AwarenessPhase,
        BehavioralPhase,
        ReintroductionPhase
    )
    private val storageKey: String = "PhaseManager"
    private var currentPhaseIndex: Int = 0

    // Métodos
    fun saveStates() {
        StorageManager.saveInt("${storageKey}_currentPhaseIndex", currentPhaseIndex)
        Log.d("PhaseManager", "Se guardan los datos de las Fases")
        for (phase in phases) {
            phase.saveState()
        }
    }

    fun loadStates() {
        currentPhaseIndex = StorageManager.getInt("${storageKey}_currentPhaseIndex", currentPhaseIndex)
        Log.d("PhaseManager", "Se cargan los datos de las Fases")
        for (phase in phases) {
            phase.loadState()
        }
    }

    fun getCurrentPhase(): Phase? = phases.getOrNull(currentPhaseIndex)

    fun canAdvancePhase(): Boolean = getCurrentPhase()?.areRequirementsMet ?: false

    fun advancePhase(): Boolean {
        if (canAdvancePhase() && currentPhaseIndex < phases.size-1) {
            currentPhaseIndex++
            return true
            // TODO("Hacer algo especial si ya no se puede avanzar más, lo de la fase de monitoreao")
        }
        return false
    }
}