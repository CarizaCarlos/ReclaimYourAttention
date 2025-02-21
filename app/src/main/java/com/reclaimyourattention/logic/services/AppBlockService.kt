package com.reclaimyourattention.logic.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.AppBlockRequestReceiver
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType

class AppBlockService: Service() {
    // Parámetros
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear

    // Variables de Control
    private var appBlockRequestReceiver: AppBlockRequestReceiver? = null

    // Métodos Superclase
    override fun onCreate() {
        // Se crea el canal de notificación
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "app_block", // ID del canal
                "Bloqueo Indefinido de Apps", // Nombre visible
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "..."
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Inicializa el Receiver para escuchar block y unblock requests
        appBlockRequestReceiver = AppBlockRequestReceiver(
            onBlockRequest = {
                // Asocia el BlockRequest a cada paquete proporcionado y lo guarda en blockedPackages
                for (appPackage in appPackages) {
                    // Crea un nuevo MutableMap<ToolType, BlockRequest> de ser necesario y asigna el nuevo blockRequest a su clave correspondiente
                    blockedPackages.getOrPut(appPackage) { mutableMapOf() }[blockRequest.tool] = blockRequest
                }
            },
            onUnblockRequest = {

            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Crea la notificación persistente
        val persistentNotification = NotificationCompat.Builder(this, "app_block")
            .setContentTitle("Servicio en primer plano")
            .setContentText("El servicio está ejecutándose en primer plano.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // Inicia el servicio en primer plano
        startForeground(1, persistentNotification)

        Log.d("AppBlockService", "Servicio Activado") // Log

        return START_STICKY
    }

    override fun onDestroy() {}

    override fun onBind(intent: Intent?): IBinder? = null
}
