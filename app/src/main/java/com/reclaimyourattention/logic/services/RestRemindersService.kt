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
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.ScreenReceiver
import com.reclaimyourattention.logic.tools.RestReminders

class RestRemindersService: Service() { // Depende de RestReminders Tool
    // Parámetros
        // Solicitados al User
        private var activityMinutesThreshold: Int = 25
        // Inmutables
        private val inactiveSecondsThreshold: Int = 5

    // Variables de Control
    private var activitySeconds = 0
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null
    private var screenReceiver: ScreenReceiver? = null

    // Runnables
    private val countRunnable = object : Runnable {
        val refreshSeconds: Int = 5
        override fun run() {
            activitySeconds += refreshSeconds

            Log.d("RestRemindersService", "Tiempo Activo: $activitySeconds seg") // Log

            // Revisa si el usuario supera el tiempo establecido
            if (activitySeconds >= activityMinutesThreshold*60) {
                sendNotification()
                activitySeconds = 0 // Reinicia el conteo
            }

            handler?.postDelayed(this, refreshSeconds.toLong()*1000) // Vuelve a ejecutar luego de refreshSeconds
        }
    }

    // Métodos Superclase
    override fun onCreate() {
        // Se crea el canal de notificación
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            "rest_reminders", // ID del canal
            "Recordatorios de Descanso", // Nombre visible
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones para recordarte descansar del celular"
        }

        notificationManager.createNotificationChannel(channel)

        // Inicializa los handler
        handlerThread = HandlerThread("RestRemindersServiceThread").apply { start() }
        handler = Handler(handlerThread!!.looper)

        // Inicializa el Receiver para escuchar cuando se enciende y apaga la pantalla
        screenReceiver = ScreenReceiver(
            onScreenOn = {
                // Detiene el reinicio de la cuenta (Si no se ha reiniciado)
                handler?.removeCallbacksAndMessages(null)

                // Empieza a contar el tiempo de actividad
                handler?.postDelayed(countRunnable, countRunnable.refreshSeconds.toLong()*1000)

                Log.d("RestRemindersService", "Se Inicia el Conteo") // Log
            },
            onScreenOff = {
                // Frena el conteo TODO("Verificar que exista un callback que borrar, porque osino se activa el handler innecesariamente")
                handler?.removeCallbacksAndMessages(null)

                // Reinicia la cuenta si se supera el threshold
                handler?.postDelayed({
                    activitySeconds = 0

                    Log.d("RestReminders", "Conteo Reiniciado") // Log
                }, inactiveSecondsThreshold.toLong()*1000)

                Log.d("RestRemindersService", "Empieza la Espera de Inactividad") // Log
            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Crea la notificación persistente
        val persistentNotification = NotificationCompat.Builder(this, "rest_reminders")
            .setContentTitle("Servicio en primer plano")
            .setContentText("El servicio está ejecutándose en primer plano.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // Inicia el servicio en primer plano
        startForeground(1, persistentNotification)

        // Crea el filtro y registra el Receiver
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenReceiver, filter)

        // Recupera los parámetros solicitados
        activityMinutesThreshold = RestReminders.activityMinutesThreshold

        Log.d("RestRemindersService", "activityMinutesThreshold: $activityMinutesThreshold") // Log

        // Empieza el proceso por primera vez
        handler?.postDelayed(countRunnable, countRunnable.refreshSeconds.toLong()*1000)

        Log.d("RestRemindersService", "Servicio Activado") // Log

        return START_STICKY
    }

    override fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        screenReceiver?.let { unregisterReceiver(it) }
        handlerThread?.quitSafely()
        handler = null
        handlerThread = null
        screenReceiver = null

        Log.d("RestRemindersService", "Servicio Terminado") // Log
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Métodos
    private fun sendNotification() {
        // Se crea la notificación
        val notification = NotificationCompat.Builder(this, "rest_reminders")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Descanso recomendado")
            .setContentText("Has pasado $activityMinutesThreshold minutos frente a la pantalla. Es hora de un descanso")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Se envía la notificación
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(1, notification)

        Log.d("RestRemindersService", "Notificación Enviada") // Log
    }
}