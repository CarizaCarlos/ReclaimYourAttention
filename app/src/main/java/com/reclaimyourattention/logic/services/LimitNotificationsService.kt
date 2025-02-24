package com.reclaimyourattention.logic.services

import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class LimitNotificationsService: NotificationListenerService() {
    companion object {
        // Variables de Control
        private var active: Boolean = false

        // Parámetros Solictados al user
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun start(context: Context) {
            active = true

            // Recupera los parámetros solicitados
            val prefs = context.getSharedPreferences("LimitNotificationsPrefs", MODE_PRIVATE)
            blockedPackages = prefs.getStringSet("blockedPackages", blockedPackages) ?: mutableSetOf()

            Log.d("LimitNotificationsService", "blockedPackages: $blockedPackages") // Log
        }

        fun stop() {
            active = false
        }
    }

    // Métodos Superclase
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!active) return

        val packageName = sbn.packageName
        Log.d("LimitNotificationsService", "Se Escucha una Notificación de: $packageName")

        // Verifica si está marcado el paquete
        if (packageName in blockedPackages) {
            cancelNotification(sbn.key)
            Log.d("LimitNotificationsService", "Se ha Bloqueado la Notificación")
        }
    }
}