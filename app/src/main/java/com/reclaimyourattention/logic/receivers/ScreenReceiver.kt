package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenReceiver(private val onScreenOn: () -> Unit, private val onScreenOff: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> onScreenOn()
            Intent.ACTION_SCREEN_OFF -> onScreenOff()
        }
    }
}