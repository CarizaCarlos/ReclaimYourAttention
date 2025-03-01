package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.StorageManager

abstract class Phase {
    // Atributos
    abstract val title: String
    abstract val description: String
    abstract val weeks: List<Set<Task>>
    protected abstract val storageKey: String

    // Variables de Control
    var currentWeekIndex: Int = 0
        protected set
    var completedTaskIDs: MutableSet<String> = mutableSetOf()
        protected set

    // Métodos
    fun saveState() {
        StorageManager.saveInt("${storageKey}_currentWeekIndex", currentWeekIndex)
        StorageManager.saveStringSet("${storageKey}_completedTaskIDs", completedTaskIDs)
    }

    fun loadState() {
        currentWeekIndex = StorageManager.getInt("${storageKey}_currentWeekIndex", currentWeekIndex)
        completedTaskIDs = StorageManager.getStringSet("${storageKey}_completedTaskIDs", completedTaskIDs) as MutableSet<String>
    }

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
            currentWeekIndex++ // TODO("Añadir el limitante de tiempo a las weeks, osea que de verdad tenga que pasar 1 week, o quizas en el manager")
        }
    }

    fun unCompleteTask(id: String) {
        completedTaskIDs.remove(id)
    }

    fun getIncompleteTasksForCurrentWeek(): List<Task> {
        val currentWeekTasks = weeks.getOrNull(currentWeekIndex) ?: emptySet()
        return currentWeekTasks.filter { !completedTaskIDs.contains(it.id) }
    }
}