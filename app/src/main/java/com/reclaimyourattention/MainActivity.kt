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
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.tools.ToolManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = this
        val appBlockService=
        setContent {
            ReclaimYourAttentionTheme {
//                PhaseScreen()
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
fun ServiceButtons(context: Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botón para activar AppBlockService
        Button(onClick = {
            context.startService(Intent(context, AppBlockService::class.java))
        }) {
            Text("Activar AppBlockService")
        }

        // Botón para abrir la configuración de accesibilidad
        OpenAccessibilitySettingsButton(context)
    }
}



@Composable
fun PhaseScreen(phaseViewModel: PhaseViewModel = viewModel()) {
    val currentPhase by phaseViewModel.currentPhase.observeAsState()
    val currentTasks by phaseViewModel.currentTasks.observeAsState(initial = emptyList())
    val canAdvance by phaseViewModel.canAdvancePhase.observeAsState(initial = false)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            currentPhase?.let { phase ->
                Text(text = phase.title, style = MaterialTheme.typography.headlineMedium)
                Text(text = phase.description, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Current week: ${phase.currentWeekIndex}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(currentTasks) { task ->
                        TaskItem(task = task) {
                            phaseViewModel.completeTask(task.id)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { phaseViewModel.onAdvancePhaseClicked() },
                    enabled = canAdvance,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Advance Phase")
                }
            } ?: Text(
                text = "All phases completed!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, onComplete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = task.body, style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = onComplete) {
                Text("Mark Completed")
            }
        }
    }
}







@Composable
fun Naveg(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("tools") { ToolsScreen(navController) }
        composable("usage") { UsageScreen(navController) }
        //RestReminder
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