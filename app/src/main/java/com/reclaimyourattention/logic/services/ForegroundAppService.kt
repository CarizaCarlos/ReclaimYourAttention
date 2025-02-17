package com.reclaimyourattention.logic.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class ForegroundAppService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            Log.d("AppFocusTracker", "App en primer plano: $packageName")
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