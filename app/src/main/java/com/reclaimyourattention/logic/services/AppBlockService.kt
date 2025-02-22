package com.reclaimyourattention.logic.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.AppBlockRequestReceiver
import com.reclaimyourattention.logic.receivers.ForegroundAppReceiver
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import com.reclaimyourattention.models.toolTypePriority
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

class AppBlockService: Service() {
    // Parámetros
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest?>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear

    // Variables de Control
    private var appBlockRequestReceiver: AppBlockRequestReceiver? = null
    private var foregroundAppReceiver: ForegroundAppReceiver? = null
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null
    private var activePackageName: String? = null
    private var activeRequest: BlockRequest? = null
    private var isUpdateRunnableActive = false

    // Runnables
    private val updateRunnable = object : Runnable {
        val refreshSeconds: Int = 5
        override fun run() {
            // Verifica si el paquete está en blockedPackages
            if (activePackageName != null && blockedPackages.containsKey(activePackageName)) {
                // Obtiene el request de más alta prioridad
                val highestPriorityRequest = getHighestPriorityRequest(activePackageName!!)
                if (highestPriorityRequest != null) {
                    // Verifica si el nuevo request de mayor prioridad es igual al activo
                    if (highestPriorityRequest != activeRequest) {
                        // Actualiza el activeRequest
                        activeRequest = highestPriorityRequest

                        // Bloquea la app usando el nuevo request
                        Log.d("AppBlockService", "Se bloqueó el paquete: $activePackageName usando $${activeRequest}") // Log TODO()
                    }

                    // Programa la siguiente llamada y se detiene
                    handler?.postDelayed(this, refreshSeconds.toLong()*1000) // Vuelve a ejecutar cada segundo
                    return
                }
            }

            // Si el paquete no existe o no contiene requests válidos
            // Desbloquea la app
            Log.d("AppBlockService", "Se desbloquea $activePackageName") // Log TODO()

            // Desactiva el Runnable
            isUpdateRunnableActive = false
            activeRequest = null
        }
    }

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

        // Inicializa los handler
        handlerThread = HandlerThread("AppBlockServiceThread").apply { start() }
        handler = Handler(handlerThread!!.looper)

        // Inicializa el Receiver para escuchar block y unblock requests
        appBlockRequestReceiver = AppBlockRequestReceiver(
            onBlockRequest = { appPackages, tool, request ->
                // Asocia el BlockRequest a cada paquete proporcionado y lo guarda en blockedPackages
                for (appPackage in appPackages) {
                    // Crea un nuevo MutableMap<ToolType, BlockRequest> de ser necesario y asigna el nuevo blockRequest a su clave correspondiente
                    blockedPackages.getOrPut(appPackage) { mutableMapOf() }[tool] = request
                    Log.d("AppBlockService", "Bloqueos de $appPackage: ${blockedPackages[appPackage]}") // Log
                }

                // Verifica si al paquete en pantalla se le aplicó un nuevo bloqueo y si no hay un updateRunnable que atrape los cambios
                if ((activePackageName in appPackages) && !isUpdateRunnableActive) {
                    // Activa el updateRunnable
                    handler?.post(updateRunnable)
                    isUpdateRunnableActive = true
                }
            },
            onUnblockRequest = { appPackages, tool ->
                // Elimina el blockRequest asignado a la tool de cada paquete
                for (appPackage in appPackages) {
                    blockedPackages[appPackage]?.remove(tool)
                    Log.d("AppBlockService", "Bloqueos de $appPackage: ${blockedPackages[appPackage]}") // Log
                }
            }
        )

        // Inicializa el Receiver para escuchar cuando se cambia de app
        foregroundAppReceiver = ForegroundAppReceiver(
            onAppChanged = { packageName ->
                activePackageName = packageName
                // Verifica si el paquete está en blockedPackages
                if (blockedPackages.containsKey(packageName)) {
                    // Obtiene el request de más alta prioridad
                    val highestPriorityRequest = getHighestPriorityRequest(packageName)
                    if (highestPriorityRequest != null) {
                        // Bloquea la app

                        Log.d("AppBlockService", "Se bloqueó el paquete: $packageName usando $${highestPriorityRequest}") // Log TODO()

                        // Actualiza activeRequest y activa el updateRunnable
                        activeRequest = highestPriorityRequest
                        handler?.postDelayed(updateRunnable, updateRunnable.refreshSeconds.toLong()*1000)
                        isUpdateRunnableActive = true
                    }
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

        // Crea el filtro y registra el AppBlockRequestReceiver
        var filter = IntentFilter("BLOCK_REQUEST").apply {
            addAction("UNBLOCK_REQUEST")
        }
        ContextCompat.registerReceiver(
            this,
            appBlockRequestReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        // Crea el filtro y registra el ForegroundAppReceiver
        filter = IntentFilter("FOREGROUND_APP_CHANGED")
        ContextCompat.registerReceiver(
            this,
            foregroundAppReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        Log.d("AppBlockService", "Servicio Activado") // Log

        return START_STICKY
    }

    override fun onDestroy() {}

    override fun onBind(intent: Intent?): IBinder? = null

    // Métodos
    private fun getHighestPriorityRequest(packageName: String): BlockRequest? {
        // Itera dentro de los requests del de mayor a menor prioridad
        for (toolType in toolTypePriority) {
            // Verifica si existe un request del toolType
            if (blockedPackages[packageName]?.containsKey(toolType) == true) {
                // Verifica si es null
                if (blockedPackages[packageName]?.get(toolType) != null) {
                    // Verifica que unblockTime no sea null
                    val blockRequest = blockedPackages[packageName]!![toolType]!!
                    if (blockRequest.unblockTime == null) {
                        // Si es de un toolType INDEFINITELY retorna el request
                        if (toolType == ToolType.INDEFINITELY) {
                            return blockRequest
                        }
                        // Elimina el request con unblockTime inválido
                        blockedPackages[packageName]?.remove(toolType)
                    }

                    // Verifica que no sea un INDEFINITELY con Fecha para evitar que se cancele innecesariamente, lanzar un Log.e
                    if (toolType == ToolType.INDEFINITELY) {
                        Log.e("AppBlockService", "El paquete $packageName contiene un request tipo INDEFINITELY con unblockTime no null")
                        return blockRequest
                    }

                    // Verifica que la fecha no haya pasado
                    if (blockRequest.unblockTime!! > Clock.System.now()) {
                        return blockRequest
                    } else {
                        // Elimina el request
                        blockedPackages[packageName]?.remove(toolType)
                    }
                } else {
                    // Elimina el request
                    blockedPackages[packageName]?.remove(toolType)
                }
            }
        }

        // Elimina la clave del paquete que no contenga bloqueos válidos
        blockedPackages.remove(packageName)

        return null
    }
}
