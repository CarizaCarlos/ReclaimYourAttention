package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import com.reclaimyourattention.logic.StorageManager
import com.reclaimyourattention.logic.services.WaitTimeForAppService

object WaitTimeForApp: Tool() {
    // Variables Superclase
    override val title: String = "Tiempo de Espera para Ingresar a Apps"
    override val description: String = "Evita que se ingrese inmediatamente a una app, antes se deberá esperar el tiempo establecido " +
        "mientras se muestra mensaje de reflexión"
    override val storageKey: String = "WaitTimeForApp"

    // Parámetros Solicitados al User
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set
    var waitSeconds: Int = 20
        private set

    // Métodos Superclase
    override fun saveState() {
        StorageManager.saveStringSet("${storageKey}_blockedPackages", blockedPackages)
        StorageManager.saveInt("${storageKey}_waitSeconds", waitSeconds)
        super.saveState()
    }

    override fun loadState() {
        blockedPackages = StorageManager.getStringSet("${storageKey}_blockedPackages", blockedPackages) as MutableSet<String>
        waitSeconds = StorageManager.getInt("${storageKey}_waitSeconds", waitSeconds)
        super.loadState()
    }

    override fun activate(vararg parameters: Any) { // waitSeconds: Int, blockedPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 2
            && parameters[0] is Int
            && parameters[1] is MutableSet<*>
            && (parameters[1] as? MutableSet<*>)?.all { it is String } == true)
        {
            waitSeconds = parameters[0] as Int
            blockedPackages = (parameters[1] as MutableSet<*>).map { it as String }.toMutableSet()
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaban exactamente 2 parámetros de tipo Int, MutableSet<String>, " +
                        "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                        "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                        "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros
        saveParameters()

        // Inicia el servicio
        appContext.startService(Intent(appContext, WaitTimeForAppService::class.java))
    }

    override fun reactivate() {
        // Guarda los parámetros
        saveParameters()

        // Inicia el servicio
        appContext.startService(Intent(appContext, WaitTimeForAppService::class.java))
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        appContext.stopService(Intent(appContext, WaitTimeForAppService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = appContext.getSharedPreferences("WaitTimeForAppPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("waitSeconds", waitSeconds)
            putStringSet("blockedPackages", blockedPackages)
            apply()
        }
    }
}