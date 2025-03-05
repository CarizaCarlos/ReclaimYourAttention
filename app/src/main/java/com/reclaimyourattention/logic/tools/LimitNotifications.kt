package com.reclaimyourattention.logic.tools

import android.content.Context
import android.util.Log
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import com.reclaimyourattention.logic.StorageManager
import com.reclaimyourattention.logic.services.LimitNotificationsService

object LimitNotifications: Tool() {
    //Variables Superclase
    override val title: String = "Limitar notificaciones"
    override val description: String = "Limitará las apps que pueden enviar notificaciones"
    override val storageKey: String = "LimitNotifications"

    // Parámetros Solicitados al User
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set

    //Métodos Superclase
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

    override fun activate(vararg parameters: Any) { // blockedPackages: MutableSet<String>
        active = true

        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 1
            && parameters[0] is MutableSet<*>
            && (parameters[0] as? MutableSet<*>)?.all { it is String } == true)
        {
            blockedPackages = (parameters[0] as MutableSet<*>).map { it as String }.toMutableSet()
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaba exactamente 1 parámetro de tipo MutableSet<String>, " +
                        "pero se ${if (parameters.size == 1) "recibió" else "recibieron"} ${parameters.size} " +
                        "${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                        "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Guarda los parámetros
        saveState()

        // Inicia el servicio
        LimitNotificationsService.start()
    }

    override fun reactivate() {
        // Inicia el servicio
        LimitNotificationsService.start()
    }

    override fun deactivate() {
        active = false

        // Frena el servicio
        LimitNotificationsService.stop()
    }
}