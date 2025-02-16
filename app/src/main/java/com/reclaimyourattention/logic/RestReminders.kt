package com.reclaimyourattention.logic

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.content.Context
import android.os.Build
import com.reclaimyourattention.R

class RestReminders(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Recordatorios para Descansar del Teléfono"
    override var description: String =
        "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros
        // Solicitados
        private var activeMinutesThreshold: Int = 25
        // Inmutables
        private val inactiveMinutesThreshold: Int = 2

    // Variables de Control
    private var activeMinutes = 0
    private var inactiveMinutes = 0

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        if (parameters.size == 1 && parameters[0] is Int) {
            activeMinutesThreshold = parameters[0] as Int
        } else {
            throw IllegalArgumentException(
                "Error en activate(): Se esperaba exactamente 1 parámetro de tipo Int, " +
                "pero se recibieron ${parameters.size} ${if (parameters.size == 1) "parámetro" else "parámetros"} +" +
                "de tipo ${parameters.map { it::class.simpleName }}."
            )
        }
        active = true
    }

    override fun deactivate() {
        active = false
    }

    // Métodos


    private fun startUsageTracking() {

    }

    private fun startInactiveTracking() {

    }

    private fun sendNotification(context: Context) {
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
    }
}