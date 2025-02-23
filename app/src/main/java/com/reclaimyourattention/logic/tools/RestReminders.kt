package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.logic.services.RestRemindersService

class RestReminders(private val context: Context): Tool() {
    // Variables Superclase
    override val title: String
        get() = "Recordatorios para Descansar del Teléfono"
    override val description: String
        get() = "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros Solicitados al User
    companion object {
        private var activityMinutesThreshold: Int = 25

        fun getActivityMinutesThreshold(): Int {
            return activityMinutesThreshold
        }
    }

    // Métodos Superclase
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
        context.startService(Intent(context, RestRemindersService::class.java))
    }

    override fun deactivate() {
        active = false
        // Frena el servicio
        context.stopService(Intent(context, RestRemindersService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = context.getSharedPreferences("RestReminderPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("activityMinutesThreshold", activityMinutesThreshold)
            apply()
        }
    }
}