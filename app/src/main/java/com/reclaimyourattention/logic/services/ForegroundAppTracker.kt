package com.reclaimyourattention.logic.services

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ForegroundAppTracker: AccessibilityService() { // Independiente, necesaria para el funcionamiento de varias Tools
    companion object {
        // Variables de Control
        @Volatile
        var lastPackageName: String? = null
            private set
    }

    // Métodos Superclase
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Obtiene el nombre del paquete
            val packageName = event.packageName?.toString() ?: return

            // Ignora la ui del sistema
            if (packageName == "com.android.systemui") { return }

            // Evita enviar paquetes repetidos
            if (packageName != lastPackageName) {
                lastPackageName = packageName

                // Guarda el paquete
                val prefs = getSharedPreferences("ForegroundAppTracker", Context.MODE_PRIVATE)
                prefs.edit().putString("lastPackageName", lastPackageName).apply()

                // Envía un Broadcast
                sendBroadcast(Intent("FOREGROUND_APP_CHANGED"))

                Log.d("ForegroundAppTracker", "Se ha emitido el Broadcast. App en primer plano: $packageName")
            }
        }
    }

    override fun onInterrupt() {
        Log.d("ForegroundAppTracker", "Servicio interrumpido")
    }

    override fun onServiceConnected() {
        Log.d("ForegroundAppTracker", "Servicio de accesibilidad conectado")
    }

    // Getters
    fun getLastPackageName(): String? {
        return lastPackageName
    }
}