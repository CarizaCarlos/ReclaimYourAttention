package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.services.RestRemindersService
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import kotlinx.serialization.json.Json

class AppBlock(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Recordatorios para Descansar del Teléfono"
    override var description: String =
        "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros Solicitados
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear

    // Persistencia
    private val prefs = context.getSharedPreferences("AppBlockPrefs", Context.MODE_PRIVATE)

    // Métodos Superclase
    override fun activate(vararg parameters: Any) { // appPackages: MutableSet<String>, blockRequest: BlockRequest
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 2
            && parameters[0] is MutableSet<*>
            && (parameters[0] as MutableSet<*>).all { it is String }
            && parameters[1] is BlockRequest)
        {
            // Asocia el BlockRequest a cada paquete proporcionado y lo guarda en blockedPackages
            val appPackages = (parameters[0] as MutableSet<*>)
                .map { it as String }
                .toMutableSet()
            val blockRequest = parameters[1] as BlockRequest

            for (appPackage in appPackages) {
                // Crea un nuevo MutableMap<ToolType, BlockRequest> de ser necesario y asigna el nuevo blockRequest a su clave correspondiente
                blockedPackages.getOrPut(appPackage) { mutableMapOf() }[blockRequest.tool] = blockRequest
            }
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaban exactamente 2 parámetros de tipo MutableSet<String>, BlockRequest, " +
                "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros solicitados
        saveParameters()

        // Inicia el servicio
        context.startService(Intent(context, AppBlockService::class.java))
    }

    override fun deactivate() {
        active = false
        // Frena el servicio
        context.stopService(Intent(context, AppBlockService::class.java))
    }

    // Métodos
    private fun saveParameters() {
        val jsonString = Json.encodeToString(blockedPackages)
        prefs.edit().putString("blockedPackages", jsonString).apply()
    }
}