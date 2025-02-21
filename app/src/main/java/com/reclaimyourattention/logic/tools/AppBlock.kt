package com.reclaimyourattention.logic.tools

import android.content.Context
import android.content.Intent
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.services.RestRemindersService
import com.reclaimyourattention.logic.services.WaitTimeForAppService
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

class AppBlock(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Recordatorios para Descansar del Teléfono"
    override var description: String =
        "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros
        // Solicitados al User
        private var indefinitelyBlockedPackages: MutableSet<String> = mutableSetOf()

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

        // Guarda los parámetros
        saveParameters()

        // Envia un blockRequest a AppBlockService
        val blockRequest = BlockRequest(
            ToolType.INDIFINITELY,
            "Mensaje AppBlock",
            null,
            false
        )
        context.sendBroadcast(Intent("BLOCK_REQUEST").putExtra(TODO()))
    }

    override fun deactivate() {
        active = false

        // Envia un unblockRequest a AppBlockService
        context.sendBroadcast(Intent("UNBLOCK_REQUEST").putExtra(TODO()))
    }

    // Métodos
    private fun saveParameters() {
        val prefs = context.getSharedPreferences("AppBlockPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putStringSet("indefinitelyBlockedPackages", indefinitelyBlockedPackages)
            apply()
        }
    }
}