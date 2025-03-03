package com.reclaimyourattention.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reclaimyourattention.logic.phases.Phase
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.phases.Task

object PhaseViewModel: ViewModel() {
    // Atributos TODO("Investigar si se puede dejar mas simple y organizado esos getters")
    private val _currentPhase = MutableLiveData<Phase?>(PhaseManager.getCurrentPhase())
    val currentPhase: LiveData<Phase?> get() = _currentPhase

    private val _currentTasks = MutableLiveData<List<Task>>().apply {
        value = PhaseManager.getCurrentPhase()?.getIncompleteTasksForCurrentWeek() ?: emptyList()
    }
    val currentTasks: LiveData<List<Task>> = _currentTasks

    private val _completedTasks = MutableLiveData<List<Task>>().apply {
        value = PhaseManager.phases
            .flatMap { it.getCompleteTasks() }
    }
    val completedTasks: LiveData<List<Task>> = _completedTasks

    private val _canAdvancePhase = MutableLiveData(PhaseManager.canAdvancePhase())
    val canAdvancePhase: LiveData<Boolean> get() = _canAdvancePhase

    // Métodos
    fun completeTask(taskId: String) {
        val phase = PhaseManager.getCurrentPhase() ?: return
        phase.completeTask(taskId)

        updateAdvanceState()
    }

    fun updateAdvanceState() {
        _canAdvancePhase.value = PhaseManager.canAdvancePhase()
        _currentPhase.value = PhaseManager.getCurrentPhase()
        _currentTasks.value = PhaseManager.getCurrentPhase()?.getIncompleteTasksForCurrentWeek() ?: emptyList()
    }

    fun onAdvancePhaseClicked() {
        if (PhaseManager.advancePhase()) {
            updateAdvanceState()
        }
    }
}