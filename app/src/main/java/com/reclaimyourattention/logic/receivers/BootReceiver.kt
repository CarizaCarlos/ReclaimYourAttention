package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.ToolManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reactiva los Servicios de ser necesario
            ToolManager.loadStates()

            AppBlockService.loadState()

            Log.d("BootReceiver", "Boot completed: Tools reloaded, services reactivated if needed.")
        }
    }
}