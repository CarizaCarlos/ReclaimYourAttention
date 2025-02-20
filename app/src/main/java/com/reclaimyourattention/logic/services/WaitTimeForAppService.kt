package com.reclaimyourattention.logic.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.ForegroundAppReceiver
import com.reclaimyourattention.logic.receivers.ScreenReceiver

class WaitTimeForAppService: Service() {
    // Parámetros
        // Solicitados al User
        private var waitSeconds: Int = 20
        private var blockedPackages: MutableSet<String> = mutableSetOf()
        // Inmutables
        private val cooldownSeconds: Int = 30

    // Variables de Control
    private var foregroundAppReceiver: ForegroundAppReceiver? = null

    // Métodos Superclase
    override fun onCreate() {
        // Inicializa el Receiver para escuchar cuando se cambia de app
        val foregroundAppReceiver = ForegroundAppReceiver(
            onAppChanged = { packageName ->
                // Verifica si el paquete está marcado
                if (packageName in blockedPackages) {
                    // Realiza una solicitúd de bloqueo a AppBlockService

                    // Faltaaa solicitud <<<<<

                    Log.d("WaitTimeForAppService", "Se Envía Solicitúd para Bloquear: $packageName") // Log
                }
            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Crea la notificación persistente
        val persistentNotification = NotificationCompat.Builder(this, "wait_time_for_app")
            .setContentTitle("Servicio en primer plano")
            .setContentText("El servicio está ejecutándose en primer plano.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // Inicia el servicio en primer plano
        startForeground(1, persistentNotification)

        // Recupera los parámetros solicitados
        val prefs = getSharedPreferences("WaitTimeForAppPrefs", MODE_PRIVATE)
        waitSeconds = prefs.getInt("waitSeconds", waitSeconds)
        blockedPackages = prefs.getStringSet("waitSeconds", blockedPackages) ?: mutableSetOf()

        Log.d("WaitTimeForAppService", "waitSeconds: $waitSeconds, blockedPackages: $blockedPackages") // Log

        // Crea el filtro y registra el Receiver
        val filter = IntentFilter("FOREGROUND_APP_CHANGED")
        ContextCompat.registerReceiver(
            this,
            foregroundAppReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        Log.d("WaitTimeForAppService", "Servicio Activado") // Log

        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(foregroundAppReceiver)
        foregroundAppReceiver = null

        Log.d("WaitTimeForAppService", "Servicio Terminado") // Log
    }

    override fun onBind(intent: Intent?): IBinder? = null
}