package com.reclaimyourattention.logic.receivers

import android.app.Service.MODE_PRIVATE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ForegroundAppReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "FOREGROUND_APP_CHANGED") {
            val prefs = context.getSharedPreferences("ForegroundAppTracker", MODE_PRIVATE)
            val lastPackageName = prefs.getString("lastPackageName", "null")
            Log.d("ForegroundAppReceiver", "Broadcast Recibido. Nueva app en pantalla: $lastPackageName")
        }
    }
}