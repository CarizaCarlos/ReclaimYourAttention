package com.reclaimyourattention.logic.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType

class AppBlockService: AccessibilityService() {
    // Parámetros Solicitados
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear

    // Variables de Control
    private var lastPackageName: String? = null

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