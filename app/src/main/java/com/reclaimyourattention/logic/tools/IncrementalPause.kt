package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.logic.services.LimitTimePerSessionService

class IncrementalPause(private val context: Context): Tool() {
    // Variables Superclase
    override val title: String
        get() = "Incrementar la Pausa"
    override val description: String
        get() = "Incrementa el tiempo para acceder a una app entre más se interactúe con el teléfono mientras se espera el desbloqueo"

    // Parámetros Solicitados al User
    companion object {
        private var incrementalSeconds: Int = 3
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getIncrementalSeconds(): Int {
            return incrementalSeconds
        }

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }

    // Métodos Superclase
    override fun activate(vararg parameters: Any) { // TODO() activeMinutesTreshold: Int, cooldownMinutes: Int, blockedPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros //TODO()
        if (parameters.size == 3
            && parameters[0] is Int
            && parameters[1] is Int
            && parameters[2] is MutableSet<*>
            && (parameters[2] as? MutableSet<*>)?.all { it is String } == true)
        {
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
        context.startService(Intent(context, LimitTimePerSessionService::class.java)) //TODO()
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        context.stopService(Intent(context, LimitTimePerSessionService::class.java)) //TODO()
    }

    // Métodos
    private fun saveParameters() { //TODO()
        val prefs = context.getSharedPreferences("LimitTimePerSessionPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putStringSet("blockedPackages", blockedPackages)
            apply()
        }
    }
}