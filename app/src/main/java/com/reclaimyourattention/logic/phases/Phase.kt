package com.reclaimyourattention.logic.phases

abstract class Phase {
    // Atributos
    protected abstract val title: String
    protected abstract val description: String
    protected abstract val weeks: List<Set<Task>>

    // Variables de Control
    private var currentWeekIndex: Int = 0
    private var completedTaskIDs: MutableSet<String> = mutableSetOf()

    // Métodos
    fun areRequirementsMet(): Boolean {
        return currentWeekIndex >= weeks.size
    }

    fun completeTask(id: String) {
        // Completar la task (Guarda su id)
        completedTaskIDs.add(id)

        // Verifica si queda completa la semana, si si, currentWeekIndex++
        val currentWeekTasks = weeks.getOrNull(currentWeekIndex) ?: return
        val allMandatoryCompleted = currentWeekTasks
            .filter { it.isMandatory }
            .all { completedTaskIDs.contains(it.id) }
        if (allMandatoryCompleted) {
            currentWeekIndex++
        }
    }

    fun unCompleteTask(id: String) {
        completedTaskIDs.remove(id)
    }

    fun getCurrentWeekIndex(): Int = currentWeekIndex
}