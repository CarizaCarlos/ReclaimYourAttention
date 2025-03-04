package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.StorageManager

abstract class Phase {
    // Atributos
    abstract val title: String
    abstract val description: String
    abstract val weeks: List<Set<Task>>
    protected abstract val storageKey: String

    // Variables de Control
    var areRequirementsMet: Boolean = false
        protected set
    var currentWeekIndex: Int = 0
        protected set
    private var completedTaskIDs: MutableSet<String> = mutableSetOf()

    // Métodos
    fun saveState() {
        StorageManager.saveInt("${storageKey}_currentWeekIndex", currentWeekIndex)
        StorageManager.saveStringSet("${storageKey}_completedTaskIDs", completedTaskIDs)
    }

    fun loadState() {
        currentWeekIndex = StorageManager.getInt("${storageKey}_currentWeekIndex", currentWeekIndex)
        completedTaskIDs = StorageManager.getStringSet("${storageKey}_completedTaskIDs", completedTaskIDs) as MutableSet<String>
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
            if (currentWeekIndex+1 == weeks.size) {
                areRequirementsMet = true
                return
            }
            currentWeekIndex++
        }
    }

    fun unCompleteTask(id: String) {
        completedTaskIDs.remove(id)

        // Verifica que al descompletar se vea reflejado areRequirementsMet
        val isMandatoryTask = weeks.any { week ->
            week.any { task ->
                task.id == id && task.isMandatory
            }
        }

        if (areRequirementsMet && isMandatoryTask) {
            areRequirementsMet = false
        }
    }

    fun getIncompleteTasksForCurrentWeek(): List<Task> {
        val currentWeekTasks = weeks.getOrNull(currentWeekIndex) ?: emptySet()
        return currentWeekTasks.filter { !completedTaskIDs.contains(it.id) }
    }

    fun getCompleteTasks(): List<Task> {
        val currentWeekTasks = weeks.getOrNull(currentWeekIndex) ?: emptySet()
        return currentWeekTasks.filter { completedTaskIDs.contains(it.id) }
    }
}