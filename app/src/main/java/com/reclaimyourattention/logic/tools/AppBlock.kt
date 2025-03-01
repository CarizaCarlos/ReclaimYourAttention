package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.logic.services.BlockRequest
import com.reclaimyourattention.logic.services.ToolType
import kotlinx.serialization.json.Json

class AppBlock(private val context: Context): Tool() {
    // Variables Superclase
    override val title: String
        get() = "Recordatorios para Descansar del Teléfono"

    override val description: String
        get() = "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros Solicitados al User
    companion object {
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }


    // Métodos Superclase
    override fun activate(vararg parameters: Any) { // appPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 1
            && parameters[0] is MutableSet<*>
            && (parameters[0] as MutableSet<*>).all { it is String })
        {
            // Envia un unblockRequest a AppBlockService para resetear las restricciones actuales
            val intent = Intent("UNBLOCK_REQUEST")
                .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
                .putExtra("toolType", Json.encodeToString(ToolType.INDEFINITELY))
            context.sendBroadcast(intent)

            // Actualzia los parámetros
            val appPackages = (parameters[0] as MutableSet<*>)
                .map { it as String }
                .toMutableSet()

            blockedPackages = appPackages
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

        // Envia un blockRequest a AppBlockService
        val blockRequest = BlockRequest(
            "Mensaje AppBlock",
            null,
            false
        )
        val intent = Intent("BLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.INDEFINITELY))
            .putExtra("blockRequest", Json.encodeToString(blockRequest))
        context.sendBroadcast(intent)

        Log.d("AppBlock", "Se Envía Solicitúd para Bloquear: $blockedPackages indefinidamente") // Log
    }

    override fun deactivate() {
        active = false

        // Envia un unblockRequest a AppBlockService
        val intent = Intent("UNBLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.INDEFINITELY))
        context.sendBroadcast(intent)
    }

    // Métodos
    private fun saveParameters() {
        val prefs = context.getSharedPreferences("AppBlockPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putStringSet("indefinitelyBlockedPackages", blockedPackages)
            apply()
        }
    }
}