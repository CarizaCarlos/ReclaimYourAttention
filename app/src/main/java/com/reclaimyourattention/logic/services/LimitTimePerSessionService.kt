package com.reclaimyourattention.logic.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LimitTimePerSessionService: Service() {
    // Parámetros
        // Solicitados al User
        private var activeMinutesTreshold: Int = 25
        private var cooldownMinutes: Int = 15
        private var blockedPackages: MutableSet<String> = mutableSetOf()
        // Inmutables
        private val inactiveMinutesTreshold: Int = 2

    // Variables de Control
    private var activeMinutes = 0
    private var inactiveMinutes = 0

    // Métodos Superclase
    override fun onCreate() {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {}

    override fun onBind(intent: Intent?): IBinder? = null
}