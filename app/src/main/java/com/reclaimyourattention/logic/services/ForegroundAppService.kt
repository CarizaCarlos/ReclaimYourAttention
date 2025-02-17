package com.reclaimyourattention.logic.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class ForegroundAppService : AccessibilityService() {
    private var lastPackageName: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Obtiene el nombre del paquete
            val packageName = event.packageName?.toString() ?: return

            // Ignora la ui del sistema
            if (packageName == "com.android.systemui") { return }

            // Evita enviar paquetes repetidos
            if (packageName != lastPackageName) {
                lastPackageName = packageName

                // Envía un Broadcast con el nombre del paquete
                val intent = Intent("com.reclaimyourattention.FOREGROUND_APP_CHANGED")
                intent.putExtra("package_name", packageName)
                sendBroadcast(intent)

                Log.d("ForegroundAppService", "App en primer plano: $packageName")
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