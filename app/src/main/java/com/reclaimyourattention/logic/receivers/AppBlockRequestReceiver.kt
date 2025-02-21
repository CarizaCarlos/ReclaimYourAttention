package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import kotlinx.serialization.json.Json.Default.decodeFromString

class AppBlockRequestReceiver(
    private val onBlockRequest: (MutableSet<String>, BlockRequest) -> Unit,
    private val onUnblockRequest: (MutableSet<String>, ToolType) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val blockedPackages: MutableSet<String>? = intent.getStringExtra("blockedPackages")
            ?.let { decodeFromString(it) }

        if (blockedPackages == null) {
            Log.e("AppBlockRequestReceiver", "BlockedPackages null.")
            return
        }

        if (intent.action == "BLOCK_REQUEST") {
            val blockRequest: BlockRequest? = intent.getStringExtra("blockRequest")
                ?.let { decodeFromString(it) }

            if (blockRequest == null) {
                Log.e("AppBlockRequestReceiver", "BlockRequest null.")
                return
            }

            Log.d("AppBlockRequestReceiver", "BlockRequest Recibido.")
            onBlockRequest(blockedPackages,blockRequest)
        }
        if (intent.action == "UNBLOCK_REQUEST") {
            val toolType: ToolType? = intent.getStringExtra("toolType")
                ?.let { decodeFromString(it) }

            if (toolType == null) {
                Log.e("AppBlockRequestReceiver", "ToolType null.")
                return
            }

            Log.d("AppBlockRequestReceiver", "UnblockRequest Recibido.")
            onUnblockRequest(blockedPackages,toolType)
        }
    }
}