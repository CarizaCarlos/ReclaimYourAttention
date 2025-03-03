package com.reclaimyourattention

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.ToolsScreen
import com.reclaimyourattention.ui.UsageScreen
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme

import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.reclaimyourattention.viewmodel.PhaseViewModel
import kotlinx.datetime.format.Padding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.tools.ToolManager
import com.reclaimyourattention.ui.theme.TaskScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReclaimYourAttentionTheme {
                Naveg()

                // Permiso pa mostrar sobre otras apps
                requestOverlayPermission(this)
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

@Composable
fun Naveg(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("tools") { ToolsScreen(navController) }
        composable("usage") { UsageScreen(navController) }
        composable("task") { TaskScreen() }
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier

    )
}

@Preview()
@Composable
fun GreetingPreview() {
    //A
    ReclaimYourAttentionTheme {
        Naveg()
        Column(modifier = Modifier.fillMaxWidth().padding(top=90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
        }
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