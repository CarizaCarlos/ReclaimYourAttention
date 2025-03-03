package com.reclaimyourattention.logic.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.ForegroundAppReceiver
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.minutes

class LimitTimePerSessionService: Service() { // Depende de LimitTimePerSession Tool
    // Parámetros
        // Solicitados al User
        private var activeMinutesTreshold: Int = 25
        private var cooldownMinutes: Int = 15
        private var blockedPackages: MutableSet<String> = mutableSetOf()
        // Inmutables
        private val inactiveMinutesTreshold: Int = 1

    // Variables de Control
    private var activeSeconds = 0
    private var areBlocked = false
    private var isCountActive = false
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null
    private var foregroundAppReceiver: ForegroundAppReceiver? = null

    // Runnables
    private val inactivityRunnable = Runnable {
        activeSeconds = 0
        Log.d("LimitTimePerSessionService", "Se reinicia el conteo") // Log
    }
    private val countRunnable = object : Runnable {
        val refreshSeconds: Int = 1
        override fun run() {
            activeSeconds += refreshSeconds

            Log.d("LimitTimePerSessionService", "Tiempo Activo: ${activeSeconds/60} min y ${activeSeconds%60} seg") // Log

            // Revisa si el usuario supera el tiempo establecido
            if (activeSeconds >= activeMinutesTreshold*60) {
                // Envia un blockRequest a AppBlockService
                val blockRequest = BlockRequest(
                    "Mensaje LimitTime",
                    Clock.System.now()+cooldownMinutes.minutes,
                    true
                )
                val intent = Intent("BLOCK_REQUEST")
                    .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
                    .putExtra("toolType", Json.encodeToString(ToolType.LIMIT_SESSION))
                    .putExtra("blockRequest", Json.encodeToString(blockRequest))
                sendBroadcast(intent)

                Log.d("LimitTimePerSessionService", "Se Envía Solicitúd para Bloquear: $blockedPackages por $cooldownMinutes min") // Log

                activeSeconds = 0 // Reinicia el conteo

                // Desactiva la lógica hasta que se desbloqueen para liberar recursos
                areBlocked = true
                Log.d("LimitTimePerSessionService", "Se desactiva la lógica") // Log

                handler?.postDelayed({
                    areBlocked = false

                    // Verifica si la app actual está marcada TODO("No ta funcionando recupera es reclaim your attention app")
                    val currentPackage = ForegroundAppTracker.lastPackageName
                    Log.d("LimitTimePerSessionService", "Paquete actual: $currentPackage") // Log
                    if (currentPackage in blockedPackages) {
                        handler?.postDelayed(this,refreshSeconds.toLong()*1000)
                        isCountActive = true
                    }

                    Log.d("LimitTimePerSessionService", "Se reactiva la lógica") // Log
                }, (cooldownMinutes*60).toLong()*1000)

                // Se destruye
                isCountActive = false
                return
            }

            handler?.postDelayed(this, refreshSeconds.toLong()*1000) // Vuelve a ejecutar luego de refreshSeconds
        }
    }

    // Métodos Superclase
    override fun onCreate() {
        // Se crea el canal de notificación
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            "limit_time_per_session", // ID del canal
            "Limitar el Uso por Sesión", // Nombre visible
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "..."
        }

        notificationManager.createNotificationChannel(channel)

        // Inicializa los handler
        handlerThread = HandlerThread("RestRemindersServiceThread").apply { start() }
        handler = Handler(handlerThread!!.looper)

        // Inicializa el Receiver para escuchar cuando se cambia de app
        foregroundAppReceiver = ForegroundAppReceiver(
            onAppChanged = { packageName ->
                // Verifica si las apps están bloqueadas
                if (areBlocked) {
                    return@ForegroundAppReceiver
                }

                // Verifica si el paquete está marcado
                if (packageName in blockedPackages) {
                    // Verifica si countRunnable no está activo
                    if (!isCountActive) {
                        // Elimina inactivityRunnable
                        handler?.removeCallbacks(inactivityRunnable)
                        Log.d("LimitTimePerSessionService", "Se muere inactivityRunnable")

                        // Ejecuta un countRunnable
                        handler?.postDelayed(countRunnable,countRunnable.refreshSeconds.toLong()*1000)
                        isCountActive = true
                    }
                } else {
                    // Verifica que countRunnable no esté activo
                    if (isCountActive) {
                        // Elimina el countRunnable
                        handler?.removeCallbacks(countRunnable)
                        isCountActive = false
                        Log.d("LimitTimePerSessionService", "Se crea inactivityRunnable")

                        // Programa un reinicio del conteo dentro de inactiveMinutesTreshold
                        handler?.postDelayed(inactivityRunnable, (inactiveMinutesTreshold*60).toLong()*1000)
                    }
                }
            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Crea la notificación persistente
        val persistentNotification = NotificationCompat.Builder(this, "limit_time_per_session")
            .setContentTitle("Servicio en primer plano")
            .setContentText("El servicio está ejecutándose en primer plano.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // Inicia el servicio en primer plano
        startForeground(1, persistentNotification)

        // Recupera los parámetros solicitados
        activeMinutesTreshold = LimitTimePerSession.activeMinutesTreshold
        cooldownMinutes = LimitTimePerSession.cooldownMinutes
        blockedPackages = LimitTimePerSession.blockedPackages

        Log.d("LimitTimePerSessionService", "activeMinutesTreshold: $activeMinutesTreshold, " +
                "cooldownMinutes: $cooldownMinutes, blockedPackages: $blockedPackages") // Log

        // Crea el filtro y registra el Receiver
        val filter = IntentFilter("FOREGROUND_APP_CHANGED")
        ContextCompat.registerReceiver(
            this,
            foregroundAppReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        Log.d("LimitTimePerSessionService", "Servicio Activado") // Log

        return START_STICKY
    }

    override fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        handlerThread?.quitSafely()
        foregroundAppReceiver?.let { unregisterReceiver(it) }
        handler = null
        handlerThread = null
        foregroundAppReceiver = null

        // Envia un unblockRequest a AppBlockService por si existe algún bloqueo activo
        val intent = Intent("UNBLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.LIMIT_SESSION))
        sendBroadcast(intent)

        Log.d("LimitTimePerSessionService", "Servicio Terminado") // Log
    }

    override fun onBind(intent: Intent?): IBinder? = null
}