package com.reclaimyourattention.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar

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

            // Botón de Tool

            // Botón de Completar / Descompletar
        }
    }
}