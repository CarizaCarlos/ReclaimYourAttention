package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import com.reclaimyourattention.logic.StorageManager
import com.reclaimyourattention.logic.services.LimitTimePerSessionService

object LimitTimePerSession: Tool() {
    // Variables Superclase
    override val title: String = "Limitar el Tiempo por Sesión"
    override val description: String = "Impide periodos prolongados e ininterrumpidos de uso"
    override val storageKey: String = "LimitTimePerSession"

    // Parámetros Solicitados al User
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set
    var activeMinutesTreshold: Int = 25
        private set
    var cooldownMinutes: Int = 15
        private set

    // Métodos Superclase
    override fun saveState() {
        super.saveState()
        StorageManager.saveStringSet("${storageKey}_blockedPackages", blockedPackages)
        StorageManager.saveInt("${storageKey}_activeMinutesTreshold", activeMinutesTreshold)
        StorageManager.saveInt("${storageKey}_cooldownMinutes", cooldownMinutes)
    }

    override fun loadState() {
        super.loadState()
        blockedPackages = StorageManager.getStringSet("${storageKey}_blockedPackages", blockedPackages) as MutableSet<String>
        activeMinutesTreshold = StorageManager.getInt("${storageKey}_activeMinutesTreshold", activeMinutesTreshold)
        cooldownMinutes = StorageManager.getInt("${storageKey}_cooldownMinutes", cooldownMinutes)
    }

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
        appContext.startService(Intent(appContext, LimitTimePerSessionService::class.java))
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        appContext.stopService(Intent(appContext, LimitTimePerSessionService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = appContext.getSharedPreferences("LimitTimePerSessionPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("activeMinutesTreshold", activeMinutesTreshold)
            putInt("cooldownMinutes", cooldownMinutes)
            putStringSet("blockedPackages", blockedPackages)
            apply()
        }
    }
}