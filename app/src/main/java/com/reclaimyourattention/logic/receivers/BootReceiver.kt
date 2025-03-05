package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.ToolManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Carga los Servicios Independientes
            AppBlockService.loadState()

            // Carga los estados guardados
            PhaseManager.loadStates()
            ToolManager.loadStates()

            Log.d("BootReceiver", "Boot completed: Tools reloaded, services reactivated if needed.")
        }
    }
}