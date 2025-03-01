package com.reclaimyourattention

import android.app.Application
import android.content.Context
import com.reclaimyourattention.logic.tools.ToolManager

class ReclaimYourAttention: Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Inicializa el Application Context
        ReclaimYourAttention.appContext = applicationContext
    }
}