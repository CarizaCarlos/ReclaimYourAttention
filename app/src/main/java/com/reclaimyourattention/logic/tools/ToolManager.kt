package com.reclaimyourattention.logic.tools

import android.util.Log

object ToolManager { // TODO("quizás para activar el AppBlock service, verificar lo de permisos o esas , tooltype priority traerlo pa'ca")
    // Atributos
    private val tools: Set<Tool> = setOf(
        AppBlock,
        BlockingScheduleForApp,
        LimitNotifications,
        LimitTimeInApp,
        LimitTimePerSession,
        RestReminders,
        WaitTimeForApp
    )

    // Métodos
    fun saveStates() {
        Log.d("ToolManager", "Se guardan los datos de las Herramientas")
        for (tool in tools) {
            tool.saveState()
        }
    }

    fun loadStates() {
        Log.d("PhaseManager", "Se cargan los datos de las Herramientas")
        for (tool in tools) {
            tool.loadState()
        }
    }
}