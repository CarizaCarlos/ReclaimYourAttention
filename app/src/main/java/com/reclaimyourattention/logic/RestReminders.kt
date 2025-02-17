package com.reclaimyourattention.logic

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.ScreenReceiver

class RestReminders(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Recordatorios para Descansar del Teléfono"
    override var description: String =
        "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros
        // Solicitados
        private var activityMinutesThreshold: Int = 1
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

            Log.d("RestReminders", "Tiempo Activo: $activitySeconds seg") // Log

            // Revisa si el usuario supera el tiempo establecido
            if (activitySeconds >= activityMinutesThreshold*60) {
                sendNotification()
                activitySeconds = 0 // Reinicia el conteo
            }

            handler?.postDelayed(this, refreshSeconds.toLong()*1000) // Vuelve a ejecutar luego de 15 segundos
        }
    }

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        // Verifica la entrada y actualiza los parámetros
        if (parameters.size == 1 && parameters[0] is Int) {
            activityMinutesThreshold = parameters[0] as Int
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaba exactamente 1 parámetro de tipo Int, " +
                "pero se recibieron ${parameters.size} ${if (parameters.size == 1) "parámetro" else "parámetros"} " +
                "de tipo ${parameters.joinToString(", ") { it::class.simpleName ?: "Desconocido" }}."
            )
        }

        // Inicializa los procesos de la herramienta
        active = true
        startMonitoring()
    }

    override fun deactivate() {
        active = false
        stopMonitoring()
    }

    // Métodos
    private fun startMonitoring() {
        // Inicializa los handler
        handlerThread = HandlerThread("RestRemindersThread").apply { start() }
        handler = Handler(handlerThread!!.looper)

        // Inicializa el Receiver para escuchar cuando se enciende y apaga la pantalla
        screenReceiver = ScreenReceiver(
            onScreenOn = {
                // Detiene el reinicio de la cuenta (Si no se ha reiniciado)
                handler?.removeCallbacksAndMessages(null)
                // Empieza a contar el tiempo de actividad
                handler?.postDelayed(countRunnable, countRunnable.refreshSeconds.toLong()*1000)

                Log.d("RestReminders", "Se Inicia el Conteo") // Log
            },
            onScreenOff = {
                // Frena el conteo
                handler?.removeCallbacksAndMessages(null)
                // Reinicia la cuenta si se supera el threshold
                Log.d("RestReminders", "Empieza la Espera de Inactividad") // Log

                handler?.postDelayed({
                    activitySeconds = 0

                    Log.d("RestReminders", "Conteo Reiniciado") // Log
                }, inactiveSecondsThreshold.toLong()*1000)
            }
        )
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        context.registerReceiver(screenReceiver, filter)

        // Empieza el proceso por primera vez
        handler?.postDelayed(countRunnable, countRunnable.refreshSeconds.toLong()*1000)
    }

    private fun stopMonitoring() {
        handler?.removeCallbacksAndMessages(null)
        screenReceiver?.let { context.unregisterReceiver(it) }
        handlerThread?.quitSafely()
        handler = null
        handlerThread = null
        screenReceiver = null
    }

    private fun sendNotification() {
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        // Se crea el canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "rest_reminder", // ID del canal
                "Recordatorios de Descanso", // Nombre visible
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones para recordarte descansar"
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Se crea la notificación
        val notification = NotificationCompat.Builder(context, "rest_reminder")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Descanso recomendado")
            .setContentText("Has pasado "+"minutos frente a la pantalla. Es hora de un descanso")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Se envía la notificación
        notificationManager.notify(1, notification)

        Log.d("RestReminders", "Notificación Enviada") // Log
    }
}