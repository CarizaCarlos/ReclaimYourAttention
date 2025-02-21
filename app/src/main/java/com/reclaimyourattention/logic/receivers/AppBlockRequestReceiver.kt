package com.reclaimyourattention.logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AppBlockRequestReceiver(
    private val onBlockRequest: () -> Unit,
    private val onUnblockRequest: () -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "BLOCK_REQUEST") {
            Log.d("AppBlockRequestReceiver", "BlockRequest Recibido.")
            onBlockRequest()
        }
        if (intent.action == "UNBLOCK_REQUEST") {
            Log.d("AppBlockRequestReceiver", "UnblockRequest Recibido.")
            onUnblockRequest()
        }
    }
}