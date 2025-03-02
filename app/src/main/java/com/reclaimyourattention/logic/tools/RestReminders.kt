package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import com.reclaimyourattention.logic.StorageManager
import com.reclaimyourattention.logic.services.RestRemindersService
import com.reclaimyourattention.logic.tools.AppBlock.blockedPackages

object RestReminders: Tool() {
    // Variables Superclase
    override val title: String = "Recordatorios para Descansar del Teléfono"
    override val description: String = "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"
    override val storageKey: String = "RestReminders"

    // Parámetros Solicitados al User
    var activityMinutesThreshold: Int = 25
        private set

    // Métodos Superclase
    override fun saveState() {
        StorageManager.saveInt("${storageKey}_activityMinutesThreshold",activityMinutesThreshold)
        super.saveState()
    }

    override fun loadState() {
        activityMinutesThreshold = StorageManager.getInt("${storageKey}_activityMinutesThreshold",activityMinutesThreshold)
        super.loadState()
    }

    override fun activate(vararg parameters: Any) { // activityMinutesThreshold: Int
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 1
            && parameters[0] is Int)
        {
            activityMinutesThreshold = parameters[0] as Int
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaba exactamente 1 parámetro de tipo Int, " +
                "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros
        saveParameters()

        // Inicia el servicio
        appContext.startService(Intent(appContext, RestRemindersService::class.java))
    }

    override fun reactivate() {
        // Guarda los parámetros
        saveParameters()

        // Inicia el servicio
        appContext.startService(Intent(appContext, RestRemindersService::class.java))
    }

    override fun deactivate() {
        active = false
        // Frena el servicio
        appContext.stopService(Intent(appContext, RestRemindersService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = appContext.getSharedPreferences("RestReminderPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("activityMinutesThreshold", activityMinutesThreshold)
            apply()
        }
    }
}