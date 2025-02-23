package com.reclaimyourattention.logic.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.receivers.AppBlockRequestReceiver
import com.reclaimyourattention.logic.receivers.ForegroundAppReceiver
import com.reclaimyourattention.models.BlockRequest
import com.reclaimyourattention.models.ToolType
import com.reclaimyourattention.models.toolTypePriority
import kotlinx.datetime.Clock
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.reclaimyourattention.ui.BlockedAppScreen

class AppBlockService: Service() {
    // Parámetros
    private var blockedPackages: MutableMap<String, MutableMap<ToolType, BlockRequest?>> = mutableMapOf() // La clave corresponde al paquete de la app a bloquear

    // Variables de Control
    private var appBlockRequestReceiver: AppBlockRequestReceiver? = null
    private var foregroundAppReceiver: ForegroundAppReceiver? = null
    private val mainHandler = Handler(Looper.getMainLooper()) // Handler del Hilo Principal
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null
    private var activePackageName: String? = null
    private var activeRequest: BlockRequest? = null
    private var isUpdateRunnableActive = false
    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null

    // Runnables
    private val updateRunnable = object : Runnable {
        val refreshSeconds: Int = 1
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
                        showBlockScreen(highestPriorityRequest)
                        Log.d("AppBlockService", "Se bloqueó el paquete: $activePackageName usando $${activeRequest}") // Log
                    }

                    // Programa la siguiente llamada y se detiene
                    handler?.postDelayed(this, refreshSeconds.toLong()*1000) // Vuelve a ejecutar cada segundo
                    return
                }
            }

            // Si el paquete no existe o no contiene requests válidos
            // Desbloquea la app
            hideBlockScreen()
            Log.d("AppBlockService", "Se desbloquea $activePackageName") // Log

            // Desactiva el Runnable
            isUpdateRunnableActive = false
            activeRequest = null
        }
    }

    // Clases Internas
    internal class MyLifecycleOwner : SavedStateRegistryOwner {
        private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
        private var mSavedStateRegistryController: SavedStateRegistryController = SavedStateRegistryController.create(this)

        override val lifecycle: Lifecycle get() = mLifecycleRegistry

        fun handleLifecycleEvent(event: Lifecycle.Event) {
            mLifecycleRegistry.handleLifecycleEvent(event)
        }

        override val savedStateRegistry = mSavedStateRegistryController.savedStateRegistry

        fun performRestore(savedState: Bundle?) {
            mSavedStateRegistryController.performRestore(savedState)
        }
    }

    // Métodos Superclase
    override fun onCreate() {
        super.onCreate()

        // Se crea el canal de notificación
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            "app_block", // ID del canal
            "Bloqueo Indefinido de Apps", // Nombre visible
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "..."
        }

        notificationManager.createNotificationChannel(channel)

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
                // Verifica que no se trate de si misma
                if (packageName != "com.reclaimyourattention") {
                    activePackageName = packageName
                    // Verifica si el paquete está en blockedPackages
                    if (blockedPackages.containsKey(packageName)) {
                        // Obtiene el request de más alta prioridad
                        val highestPriorityRequest = getHighestPriorityRequest(packageName)
                        if (highestPriorityRequest != null) {
                            // Bloquea la app
                            showBlockScreen(highestPriorityRequest)
                            Log.d("AppBlockService", "Se bloqueó el paquete: $packageName usando $${highestPriorityRequest}") // Log

                            // Actualiza activeRequest y activa el updateRunnable
                            activeRequest = highestPriorityRequest
                            handler?.postDelayed(updateRunnable, updateRunnable.refreshSeconds.toLong()*1000)
                            isUpdateRunnableActive = true
                        }
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
                    if (blockRequest.unblockTime != null) {
                        // Verifica que no sea un INDEFINITELY con Fecha para evitar que se cancele innecesariamente, lanzar un Log.e
                        if (toolType == ToolType.INDEFINITELY) {
                            Log.e("AppBlockService", "El paquete $packageName contiene un request tipo INDEFINITELY con unblockTime no null")
                            return blockRequest
                        }

                        // Verifica que la fecha no haya pasado
                        if (blockRequest.unblockTime > Clock.System.now()) {
                            return blockRequest
                        }
                    } else {
                        // Si es de un toolType INDEFINITELY retorna el request
                        if (toolType == ToolType.INDEFINITELY) {
                            return blockRequest
                        }
                    }
                }
                // Elimina el request con unblockTime inválido
                blockedPackages[packageName]?.remove(toolType)
            }
        }

        // Elimina la clave del paquete que no contenga bloqueos válidos
        blockedPackages.remove(packageName)

        return null
    }

    private fun showBlockScreen(request: BlockRequest) {
        if (overlayView != null) return
        mainHandler.post {
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            )

            val composeView = ComposeView(this)
            composeView.setContent {
                BlockedAppScreen(request)
            }

            // Trick The ComposeView into thinking we are tracking lifecycle
            val viewModelStore = ViewModelStore()
            val lifecycleOwner = MyLifecycleOwner()
            lifecycleOwner.performRestore(null)
            lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            composeView.setViewTreeLifecycleOwner(lifecycleOwner)

            val viewModelStoreOwner = object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore = viewModelStore
            }
            composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)

            composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

            windowManager.addView(composeView, params)
            overlayView = composeView
        }
    }

    private fun hideBlockScreen() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }
}
