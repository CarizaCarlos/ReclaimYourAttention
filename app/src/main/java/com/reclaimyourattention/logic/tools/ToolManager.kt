package com.reclaimyourattention.logic.tools

object ToolManager { // TODO("quizás para activar el AppBlock service, verificar lo de permisos o esas , tooltype priority traerlo pa'ca")
    // Atributos
    private val tools: Set<Tool> = setOf(
        AppBlock,
        BlockingScheduleForApp,
        Diagnostic,
        IncrementalPause,
        LimitNotifications,
        LimitTimeInApp,
        LimitTimePerSession,
        RestReminders,
        WaitTimeForApp
    )

    // Métodos
    fun saveStates() {
        for (tool in tools) {
            tool.saveState()
        }
    }

    fun loadStates() {
        for (tool in tools) {
            tool.loadState()
        }
    }
}