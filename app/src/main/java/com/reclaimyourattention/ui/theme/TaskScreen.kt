package com.reclaimyourattention.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.TaskViewModel

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    // Ejemplo Task
    val task = Task(
        id = "01",
        title = "Beneficios de la Escala de Grices y Cómo Activarla",
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nulla tellus nibh, tempor quis metus sed, malesuada porta justo. " +
                "Curabitur pellentesque leo purus, eu ultrices felis scelerisque sit amet. " +
                "Quisque vitae ultricies quam, quis facilisis nibh. " +
                "Curabitur accumsan tellus et urna rutrum lobortis. " +
                "Nam scelerisque ante odio, a dignissim massa vehicula ac. " +
                "Nullam pharetra purus eget dui venenatis gravida. " +
                "Cras id risus at justo volutpat pharetra sed at ligula. " +
                "Quisque purus odio, vehicula nec felis vitae, luctus pellentesque nisl. " +
                "Proin sodales scelerisque nisi, et blandit augue finibus eget. " +
                "Aliquam ac ligula lacinia mi vulputate tristique ut a orci. " +
                "Mauris imperdiet tempus augue eu scelerisque.\n" +

                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nulla tellus nibh, tempor quis metus sed, malesuada porta justo. " +
                "Curabitur pellentesque leo purus, eu ultrices felis scelerisque sit amet. " +
                "Quisque vitae ultricies quam, quis facilisis nibh. " +
                "Curabitur accumsan tellus et urna rutrum lobortis. " +
                "Nam scelerisque ante odio, a dignissim massa vehicula ac. " +
                "Nullam pharetra purus eget dui venenatis gravida. " +
                "Cras id risus at justo volutpat pharetra sed at ligula. " +
                "Quisque purus odio, vehicula nec felis vitae, luctus pellentesque nisl. " +
                "Proin sodales scelerisque nisi, et blandit augue finibus eget. " +
                "Aliquam ac ligula lacinia mi vulputate tristique ut a orci. " +
                "Mauris imperdiet tempus augue eu scelerisque.",
        tool = ToolType.LIMIT_DAILY,
        taskPrerrequisitesID = null,
        isMandatory = true,
        readingMinutes = 4
    )

    ReclaimYourAttentionTheme {
        TaskScreen()
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TaskScreen() {

    // Recupera la tarea
    val task by TaskViewModel.selectedTask.collectAsState()
    LaunchedEffect(task) {
        Log.d("TASKSCREEN", "Tarea recibida: ${task?.title ?: "null"}")
    }

    Log.d("TaskScreen", "Task: $task")

    Scaffold(
        modifier = Modifier
    ) { paddingValues ->
        if (task != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Informaciónd e la Task
                Text(
                    text = task!!.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = task!!.body,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Botón de Tool

                // Botón de Completar / Descompletar

            }
        } else {
            // Mensaje si la tarea no está disponible
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error, tarea null")
            }
        }
    }
}