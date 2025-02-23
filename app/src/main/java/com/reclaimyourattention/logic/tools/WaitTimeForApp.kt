package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.logic.services.WaitTimeForAppService

class WaitTimeForApp(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Tiempo de Espera para Ingresar a Apps"
    override var description: String =
        "Evita que se ingrese inmediatamente a una app, antes se deberá esperar el tiempo establecido " +
        "mientras se muestra mensaje de reflexión"

    // Parámetros Solicitados al User
    private var waitSeconds: Int = 20
    private var blockedPackages: MutableSet<String> = mutableSetOf()

    // Métodos Superclase
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
        context.startService(Intent(context, WaitTimeForAppService::class.java))
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        context.stopService(Intent(context, WaitTimeForAppService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = context.getSharedPreferences("WaitTimeForAppPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("waitSeconds", waitSeconds)
            putStringSet("blockedPackages", blockedPackages)
            apply()
        }
    }
}