package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.logic.services.LimitTimePerSessionService

class LimitTimePerSession(private val context: Context): Tool() {
    // Variables Superclase
    override val title: String
        get() = "Limitar el Tiempo por Sesión"
    override val description: String
        get() = "Impide periodos prolongados e ininterrumpidos de uso"

    // Parámetros Solicitados al User
    companion object {
        private var activeMinutesTreshold: Int = 25
        private var cooldownMinutes: Int = 15
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getActiveMinutesTreshold(): Int {
            return activeMinutesTreshold
        }

        fun getCooldownMinutes(): Int {
            return cooldownMinutes
        }

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }

    // Métodos Superclase
    override fun activate(vararg parameters: Any) { // activeMinutesTreshold: Int, cooldownMinutes: Int, blockedPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 3
            && parameters[0] is Int
            && parameters[1] is Int
            && parameters[2] is MutableSet<*>
            && (parameters[2] as? MutableSet<*>)?.all { it is String } == true)
        {
            activeMinutesTreshold = parameters[0] as Int
            cooldownMinutes = parameters[1] as Int
            blockedPackages = (parameters[2] as MutableSet<*>).map { it as String }.toMutableSet()
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaban exactamente 3 parámetros de tipo Int, Int, MutableSet<String>, " +
                        "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                        "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                        "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros
        saveParameters()

        // Inicia el servicio
        context.startService(Intent(context, LimitTimePerSessionService::class.java))
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        context.stopService(Intent(context, LimitTimePerSessionService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = context.getSharedPreferences("LimitTimePerSessionPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("activeMinutesTreshold", activeMinutesTreshold)
            putInt("cooldownMinutes", cooldownMinutes)
            putStringSet("blockedPackages", blockedPackages)
            apply()
        }
    }
}