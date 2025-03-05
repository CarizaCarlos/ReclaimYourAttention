package com.reclaimyourattention.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.ui.theme.Gray
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.livedata.observeAsState
import com.reclaimyourattention.logic.phases.PhaseManager
import com.reclaimyourattention.logic.tools.LimitNotifications
import com.reclaimyourattention.logic.tools.LimitTimeInApp
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.Tool
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.viewmodel.PhaseViewModel
import com.reclaimyourattention.viewmodel.PhaseViewModel.completedTasks

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    // Ejemplo Task
    val task = Task(
        id = "01",
        title = "Beneficios de la Escala de Grices y Cómo Activarla",
        tool = ToolType.LIMIT_DAILY,
        taskPrerrequisitesID = null,
        isMandatory = true,
        readingMinutes = 4
    )

    ReclaimYourAttentionTheme {
        TaskContent(task,null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TaskScreen(navController: NavController? = null) {
    // Recupera la tarea
    val task by TaskViewModel.selectedTask.collectAsState()

    task?.let { TaskContent(it, navController) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskContent(task: Task, navController: NavController?) {
    val completedTasks by PhaseViewModel.completedTasks.observeAsState(emptyList())
    val isCompleted = completedTasks.any { it.id == task.id }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón para Regresar
            IconButton(
                onClick = { navController?.navigateUp() },
                modifier = Modifier.size(48.dp) // Tamaño del área clickeable
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar", // Descripción para accesibilidad
                    tint = Gray
                )
            }

            // Información de la Task
            Text(
                text = task.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = task.body,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (task.tool != null) {
                val tool = when (task.tool) {
                    ToolType.WAIT_TIME -> WaitTimeForApp
                    ToolType.LIMIT_SESSION -> LimitTimePerSession
                    ToolType.LIMIT_DAILY -> LimitTimeInApp
                    ToolType.REST_REMINDERS -> RestReminders
                    ToolType.LIMIT_NOTIFICATIONS -> LimitNotifications
                }

                Button(
                    onClick = {
                        ToolViewModel.selectTool(tool)
                        navController?.navigate("toolInfo")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a la Herramienta")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón de Completar/Descompletar
            Button(
                onClick = {
                    if (isCompleted) {
                        PhaseViewModel.unCompleteTask(task.id)
                    } else {
                        PhaseViewModel.completeTask(task.id)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Gray else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isCompleted) "Descompletar" else "Completar")
            }
        }
    }
}