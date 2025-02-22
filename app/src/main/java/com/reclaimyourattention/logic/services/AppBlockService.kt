package com.reclaimyourattention.logic.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.AppBlockRequestReceiver
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import com.reclaimyourattention.models.toolTypePriority

class AppBlockService: Service() {
    // Parámetros
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest?>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear

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
            onBlockRequest = { appPackages, tool, request ->
                // Asocia el BlockRequest a cada paquete proporcionado y lo guarda en blockedPackages
                for (appPackage in appPackages) {
                    // Crea un nuevo MutableMap<ToolType, BlockRequest> de ser necesario y asigna el nuevo blockRequest a su clave correspondiente
                    Log.d("AppBlockService", "Bloqueos de $appPackage: ${blockedPackages[appPackage]}") // Log
                    blockedPackages.getOrPut(appPackage) { mutableMapOf() }[tool] = request
                    Log.d("AppBlockService", "Bloqueos de $appPackage: ${blockedPackages[appPackage]}") // Log
                }
            },
            onUnblockRequest = { appPackages, tool ->
                // Elimina el blockRequest asignado a la tool de cada paquete
                for (appPackage in appPackages) {
                    Log.d("AppBlockService", "Bloqueos de $appPackage: ${blockedPackages[appPackage]}") // Log
                    blockedPackages[appPackage]?.remove(tool)
                    Log.d("AppBlockService", "Bloqueos de $appPackage: ${blockedPackages[appPackage]}") // Log
                }
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

        // Crea el filtro y registra el Receiver
        val filter = IntentFilter("BLOCK_REQUEST").apply {
            addAction("UNBLOCK_REQUEST")
        }
        ContextCompat.registerReceiver(
            this,
            appBlockRequestReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        Log.d("AppBlockService", "Servicio Activado") // Log

        return START_STICKY
    }

    override fun onDestroy() {}

    override fun onBind(intent: Intent?): IBinder? = null

    // Métodos
    fun getHighestPriorityBlockRequest(packageName: String): BlockRequest? {
        // Verifica que el paquete esté bloqueado
        if (!blockedPackages.containsKey(packageName)) {
            return null
        }

        // Itera dentro de los requests y retorna el válido de mayor prioridad
        for (toolType in toolTypePriority) {
            val blockRequest = blockedPackages[packageName]?.get(toolType)
            if (blockRequest != null) {
                return blockRequest
            }
        }

        // Elimina la clave que no contenga bloqueos válidos
        blockedPackages.remove(packageName)

        return null
    }
}
