package com.reclaimyourattention.logic.tools

import android.content.Context
import com.reclaimyourattention.logic.services.LimitNotificationsService

class LimitNotifications(private val context: Context): Tool() {
    //Variables Superclase
    override val title: String
        get() = "Limitar notificaciones"
    override val description: String
        get() = "Limitará las apps que pueden enviar notificaciones"

    companion object {
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }

    //Parámetros
    private var appsToLimit: String = "Va a ser un ArrayList"

    //Métodos Superclase
    override fun activate(vararg parameters: Any) { // blockedPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 1
            && parameters[0] is MutableSet<*>
            && (parameters[0] as? MutableSet<*>)?.all { it is String } == true)
        {
            blockedPackages = (parameters[0] as MutableSet<*>).map { it as String }.toMutableSet()
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaba exactamente 1 parámetro de tipo MutableSet<String>, " +
                        "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                        "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                        "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros
        saveParameters()

        // Inicia el servicio
        LimitNotificationsService.start(context)
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        LimitNotificationsService.stop()
    }

    // Métodos
    private fun saveParameters() {
        val prefs = context.getSharedPreferences("LimitNotificationsPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putStringSet("blockedPackages", blockedPackages)
            apply()
        }
    }
}