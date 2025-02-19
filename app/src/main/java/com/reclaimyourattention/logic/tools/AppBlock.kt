package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.logic.services.RestRemindersService
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import kotlinx.serialization.json.Json

class AppBlock(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Recordatorios para Descansar del Teléfono"
    override var description: String =
        "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear
        // Solicitados al User
        private var indefinitelyBlockedPackages: MutableSet<String> = mutableSetOf()

    // Persistencia
    private val prefs = context.getSharedPreferences("AppBlockPrefs", Context.MODE_PRIVATE)

    // Métodos Superclase
    override fun activate(vararg parameters: Any) { // appPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 2
            && parameters[0] is MutableSet<*>
            && (parameters[0] as MutableSet<*>).all { it is String })
        {
            val appPackages = (parameters[0] as MutableSet<*>)
                .map { it as String }
                .toMutableSet()

            indefinitelyBlockedPackages = appPackages
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaba exactamente 1 parámetro de tipo MutableSet<String>, " +
                "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros y Actualiza el AppBlockService
        saveParameters()
        val intent = Intent(".APPBLOCK_BROADCAST")
        intent.putExtra("message", "Activate")
        context.sendBroadcast(intent)
    }

    override fun deactivate() {
        active = false
        // Frena el servicio
        val intent = Intent(".APPBLOCK_BROADCAST")
        intent.putExtra("message", "Deactivate")
        context.sendBroadcast(intent)
    }

    // Métodos
    public fun blockApps(appPackages: MutableSet<String>, blockRequest: BlockRequest) { // Usado internamente por otras clases
        // Asocia el BlockRequest a cada paquete proporcionado y lo guarda en blockedPackages
        for (appPackage in appPackages) {
            // Crea un nuevo MutableMap<ToolType, BlockRequest> de ser necesario y asigna el nuevo blockRequest a su clave correspondiente
            blockedPackages.getOrPut(appPackage) { mutableMapOf() }[blockRequest.tool] = blockRequest
        }

        // Guarda los parámetros y Actualiza el AppBlockService
        saveParameters()
        val intent = Intent(".APPBLOCK_BROADCAST")
        intent.putExtra("message", "BlockApps")
        context.sendBroadcast(intent)
    }

    private fun saveParameters() {
        val jsonString = Json.encodeToString(blockedPackages)
        prefs.edit().putString("blockedPackages", jsonString).apply()

        prefs.edit().putStringSet("indefinitelyBlockedPackages", indefinitelyBlockedPackages).apply()
    }
}