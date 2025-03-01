package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.logic.services.BlockRequest
import com.reclaimyourattention.logic.services.ToolType
import kotlinx.serialization.json.Json.Default.decodeFromString

class AppBlockRequestReceiver(
    private val onBlockRequest: (MutableSet<String>, ToolType, BlockRequest) -> Unit,
    private val onUnblockRequest: (MutableSet<String>, ToolType) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Revisa que contenga blockedPackages
        val blockedPackages: MutableSet<String>? = intent.getStringExtra("blockedPackages")
            ?.let { decodeFromString(it) }
        if (blockedPackages == null) {
            Log.e("AppBlockRequestReceiver", "BlockedPackages null.")
            return
        }

        // Revisa que contenga toolType
        val toolType: ToolType? = intent.getStringExtra("toolType")
            ?.let { decodeFromString(it) }
        if (toolType == null) {
            Log.e("AppBlockRequestReceiver", "ToolType null.")
            return
        }

        if (intent.action == "BLOCK_REQUEST") {
            // Revisa que contenga blockRequest
            val blockRequest: BlockRequest? = intent.getStringExtra("blockRequest")
                ?.let { decodeFromString(it) }
            if (blockRequest == null) {
                Log.e("AppBlockRequestReceiver", "BlockRequest null.")
                return
            }

            Log.d("AppBlockRequestReceiver", "BlockRequest Recibido.")
            onBlockRequest(blockedPackages,toolType,blockRequest)
        }
        if (intent.action == "UNBLOCK_REQUEST") {
            Log.d("AppBlockRequestReceiver", "UnblockRequest Recibido.")
            onUnblockRequest(blockedPackages,toolType)
        }
    }
}