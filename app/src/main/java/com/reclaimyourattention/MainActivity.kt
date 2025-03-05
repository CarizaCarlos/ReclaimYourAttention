package com.reclaimyourattention

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.ToolsScreen
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.services.RestRemindersService
import com.reclaimyourattention.logic.tools.ToolManager
import com.reclaimyourattention.ui.GetLimitNotifications
import com.reclaimyourattention.ui.TaskScreen
import com.reclaimyourattention.ui.NavigationBar
import com.reclaimyourattention.ui.ToolScreen
import com.reclaimyourattention.ui.ToolsScreens.AppBlockViewModel
import com.reclaimyourattention.ui.ToolsScreens.AppBlockViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga los Servicios Independientes
        AppBlockService.loadState()

        // Carga los estados guardados
        PhaseManager.loadStates()
        ToolManager.loadStates()

        // Permisos TODO()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
        // startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        // startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))

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
}

fun requestOverlayPermission(context: Context) {
    if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        context.startActivity(intent)
    }
}

@Composable
fun OpenAccessibilitySettingsButton(context: Context) {
    Button(onClick = { openAccessibilitySettings(context) }) {
        Text("Activar servicio de accesibilidad")
    }
}

fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}