package com.reclaimyourattention.logic.services

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ForegroundAppTracker: AccessibilityService() {
    // Variables de Control
    private var lastPackageName: String? = null

    // Persistencia
    private val prefs = getSharedPreferences("ForegroundAppTracker", Context.MODE_PRIVATE)

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
                prefs.edit().putString("lastPackageName", lastPackageName).apply()

                // Envía un Broadcast
                sendBroadcast(Intent("com.reclaimyourattention.FOREGROUND_APP_CHANGED"))

                Log.d("ForegroundAppTracker", "Se ha emitido el Broadcast. App en primer plano: $packageName")
            }
        }
    }

    override fun onInterrupt() {
        Log.d("AppFocusTracker", "Servicio interrumpido")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("AppFocusTracker", "Servicio de accesibilidad conectado")
    }
}