package com.reclaimyourattention.logic

import android.app.NotificationManager
import android.app.usage.UsageStatsManager
import androidx.core.app.NotificationCompat
import android.content.Context

class RestReminders(private val context: Context): Tool() {
    // Variables Superclase
    override var title: String = "Recordatorios para Descansar del Teléfono"
    override var description: String =
        "Envía notificaciones para recordarte de descansar la vista si has estado usando mucho el celular"

    // Parámetros
        // Solicitados
        private var activeMinutesTreshold: Int = 25
        // Inmutables
        private val inactiveMinutesTreshold: Int = 2

    // Variables
    private var activeMinutes = 0
    private var inactiveMinutes = 0

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        active = true
    }

    // Métodos
    private fun sendNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "rest_reminder")
            .setContentTitle("Descanso recomendado")
            .setContentText("Has pasado "+activeMinutes+"minutos frente a la pantalla. Es hora de un descanso")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}