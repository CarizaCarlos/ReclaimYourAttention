package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import com.reclaimyourattention.logic.StorageManager
import com.reclaimyourattention.logic.services.BlockRequest
import com.reclaimyourattention.logic.services.ToolType
import kotlinx.serialization.json.Json

object AppBlock: Tool() {
    // Variables Superclase
    override val title: String = "Bloqueo de Apps"
    override val description: String = "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"
    override val storageKey: String = "AppBlock"

    // Parámetros Solicitados al User
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set

    // Métodos Superclase
    override fun saveState() {
        StorageManager.saveStringSet("${storageKey}_blockedPackages", blockedPackages)
        super.saveState()

        Log.d(storageKey, "Datos guardados: active: $active, blockedPackages: $blockedPackages")
    }

    override fun loadState() {
        blockedPackages = StorageManager.getStringSet("${storageKey}_blockedPackages", blockedPackages).toMutableSet()
        super.loadState()

        Log.d(storageKey, "Datos cargados: active: $active, blockedPackages: $blockedPackages")
    }

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
                .putExtra("toolType", Json.encodeToString(ToolType.INDIFINITELY))
            appContext.sendBroadcast(intent)

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

        // Envia un blockRequest a AppBlockService
        val blockRequest = BlockRequest(
            "Mensaje AppBlock",
            null,
            false
        )
        val intent = Intent("BLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.INDIFINITELY))
            .putExtra("blockRequest", Json.encodeToString(blockRequest))
        appContext.sendBroadcast(intent)

        Log.d("AppBlock", "Se Envía Solicitúd para Bloquear: $blockedPackages indefinidamente") // Log
    }

    override fun reactivate() {
        // Envia un blockRequest a AppBlockService
        val blockRequest = BlockRequest(
            "Mensaje AppBlock",
            null,
            false
        )
        val intent = Intent("BLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.INDIFINITELY))
            .putExtra("blockRequest", Json.encodeToString(blockRequest))
        appContext.sendBroadcast(intent)

        Log.d("AppBlock", "Se Envía Solicitúd para Bloquear: $blockedPackages indefinidamente") // Log
    }

    override fun deactivate() {
        active = false

        // Envia un unblockRequest a AppBlockService
        val intent = Intent("UNBLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.INDIFINITELY))
        appContext.sendBroadcast(intent)
    }
}