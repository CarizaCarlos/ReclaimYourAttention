package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenEventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("PhoneUsageTime", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val currentTime = System.currentTimeMillis()

        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> {
                editor.putLong("lastUnlockTime", currentTime)
                editor.apply()
            }
            Intent.ACTION_SCREEN_OFF -> {
                editor.putLong("lastLockTime", currentTime)
                editor.apply()
            }
        }
    }
}