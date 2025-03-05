package com.reclaimyourattention

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.ToolsScreen
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.ToolManager
import com.reclaimyourattention.ui.TaskScreen
import com.reclaimyourattention.ui.NavigationBar
import com.reclaimyourattention.ui.ToolScreen
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.net.Uri
import android.view.accessibility.AccessibilityManager
import com.reclaimyourattention.logic.receivers.ForegroundAppReceiver
import com.reclaimyourattention.logic.services.LimitNotificationsService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga los Servicios Independientes
        AppBlockService.loadState()

        // Carga los estados guardados
        PhaseManager.loadStates()
        ToolManager.loadStates()

        // Permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
        if (!isAccessibilityServiceEnabled(this, ForegroundAppReceiver::class.java)) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        if (!isNotificationListenerEnabled(this)) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        enableEdgeToEdge()
        setContent {
            ReclaimYourAttentionTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                Scaffold(
                    bottomBar = {
                        NavigationBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier
                            .padding(innerPadding)
                            .navigationBarsPadding()
                    ) {
                        // Pantallas principales
                        composable("main") { MainScreen(navController) }
                        composable("tools") { ToolsScreen(navController) }
                        composable("task") { TaskScreen(navController) }
                        composable("toolInfo"){ ToolScreen(navController) }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        // Guarda los estados
        PhaseManager.saveStates()
        ToolManager.saveStates()
    }

    private fun isAccessibilityServiceEnabled(context: Context, service: Class<ForegroundAppReceiver>): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.any { it.resolveInfo.serviceInfo.name == service.name }
    }

    fun isNotificationListenerEnabled(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(ComponentName(context, LimitNotificationsService::class.java).flattenToString())
    }
}