package com.reclaimyourattention.logic.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

class BlockingScheduleForAppService: Service() {
    // Parámetros Solicitados al user
    private var blockingSchedule: Map<DayOfWeek, MutableList<Pair<LocalDateTime, LocalDateTime>>> =
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
            .associateWith { mutableListOf() }
    private var blockedPackages: MutableSet<String> = mutableSetOf()

    // Variables de Control
    private val mainHandler = Handler(Looper.getMainLooper()) // Handler del Hilo Principal
    private val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Runnables
    private val programAlarms = object : Runnable {
        override fun run() {
            // Identifica que día de la semana es hoy
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .dayOfWeek

            // Itera los bloques de tiempo de hoy
            blockingSchedule[today]?.forEachIndexed { index, timeBlock ->
                val (inicio, fin) = timeBlock
                // Verifica si el bloque no ha pasado ya
                if (fin > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) {
                    // Si no ha pasado programa su bloqueo
                    scheduleBlock(inicio, fin, index)
                }
            }
        }
    }

    // Métodos Superclase
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        removeAlarms()

        // TODO("Actualziar la info, incluye la función que fusiona los timeblocks solapados,
        //  guardar en shared prefs para que alr einicia el teléfono se puedan reprogramar las alarmas")

        mainHandler.post(programAlarms)

        return START_STICKY
    }

    override fun onDestroy() {

        removeAlarms()

        // Envia un unblockRequest a AppBlockService por si existe algún bloqueo activo
        val intent = Intent("UNBLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.SCHEDULED_BLOCK))
        sendBroadcast(intent)

        Log.d("WaitTimeForAppService", "Servicio Terminado") // Log
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Métodos
    private fun removeAlarms() {
        // Identifica que día de la semana es hoy
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .dayOfWeek

        for (index in 0 until (blockingSchedule[today]?.size ?: 0)) {
            // Recrea la estructura de pending intent utilizada para crear las alarmas
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                index,
                Intent("BLOCK_REQUEST"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Cancela la alarma usando el pending intent
            alarmManager.cancel(pendingIntent)
            Log.d("WaitTimeForAppService", "Alarma cancelada con request code: $index")
        }
    }

    private fun scheduleBlock(start: LocalDateTime, end: LocalDateTime, index: Int) {
        // Programa el envío de un blockRequest a AppBlockService
        val blockRequest = BlockRequest(
            "Mensaje Schedule",
            end.toInstant(TimeZone.currentSystemDefault()),
            false
        )

        val intent = Intent("BLOCK_REQUEST")
            .putExtra("blockedPackages", Json.encodeToString(blockedPackages))
            .putExtra("toolType", Json.encodeToString(ToolType.SCHEDULED_BLOCK))
            .putExtra("blockRequest", Json.encodeToString(blockRequest))

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            index, // Identificador único usando el index
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = start
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        // Obtenemos el AlarmManager y programamos la alarma de forma exacta.
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            Log.d("WaitTimeForAppService", "Se Programa Solicitúd para Bloquear: $blockedPackages desde $start hasta $end") // Log
        } catch (e: SecurityException) {
            Log.e("WaitTimeForAppService", "No se pudo programar la alarma: ${e.message}")
            // TODO("Pedir activar el permiso")
        }
    }
}