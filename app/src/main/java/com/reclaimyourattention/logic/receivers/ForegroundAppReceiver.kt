package com.reclaimyourattention.logic.receivers

import android.app.Service.MODE_PRIVATE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ForegroundAppReceiver(
    private val onAppChanged: (String) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "FOREGROUND_APP_CHANGED") {
            // Recupera el paquete
            val prefs = context.getSharedPreferences("ForegroundAppTracker", MODE_PRIVATE)
            val lastPackageName = prefs.getString("lastPackageName", "null")

            Log.d("ForegroundAppReceiver", "Broadcast Recibido. Nueva app en pantalla: $lastPackageName")

            // Ejecuta el lambda
            if (lastPackageName != null) {
                onAppChanged(lastPackageName)
            }
        }
    }

    /* Para Implementarlo en los Services:
        val foregroundAppReceiver = ForegroundAppReceiver()
        val filter = IntentFilter("FOREGROUND_APP_CHANGED")
        ContextCompat.registerReceiver(
            this,
            foregroundAppReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
     */
}