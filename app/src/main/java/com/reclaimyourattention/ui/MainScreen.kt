package com.reclaimyourattention.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaimyourattention.R
import com.reclaimyourattention.ReclaimYourAttention
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import com.reclaimyourattention.viewmodel.PhaseViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.ui.theme.Blue
import com.reclaimyourattention.ui.theme.DarkGray
import com.reclaimyourattention.ui.theme.Gray
import com.reclaimyourattention.ui.theme.Green
import com.reclaimyourattention.ui.theme.Purple

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ReclaimYourAttentionTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen(navController: NavController? = null) {
    Scaffold(
        modifier = Modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fase",
                style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Descripción Fase",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = "Obligatorias",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Herramientas",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(
                        text = "Opcionales",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            TaskItemsPreview()
        }
    }
}

//@Composable
//fun PhaseScreen(phaseViewModel: PhaseViewModel = viewModel()) {
//    val currentPhase by phaseViewModel.currentPhase.observeAsState()
//    val currentTasks by phaseViewModel.currentTasks.observeAsState(initial = emptyList())
//    val canAdvance by phaseViewModel.canAdvancePhase.observeAsState(initial = false)
//
//    Surface(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            currentPhase?.let { phase ->
//                Text(text = phase.title, style = MaterialTheme.typography.headlineMedium)
//                Text(text = phase.description, style = MaterialTheme.typography.bodyMedium)
//                Text(
//                    text = "Current week: ${phase.currentWeekIndex}",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                LazyColumn {
//                    items(currentTasks) { task ->
//                        TaskItem(task = task)
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = { phaseViewModel.onAdvancePhaseClicked() },
//                    enabled = canAdvance,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Advance Phase")
//                }
//            } ?: Text(
//                text = "All phases completed!",
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun TaskItemsPreview() {
    // Ejemplos Task
    val pendingTasks = listOf(
        Task(
            id = "01",
            title = "Beneficios de la Escala de Grices y Cómo Activarla",
            body = "",
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = true,
            readingMinutes = 4
        ),
        Task(
            id = "02",
            title = "¿Cómo Funciona la App?",
            body = "",
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = false,
            readingMinutes = 1
        ),
        Task(
            id = "03",
            title = "Agrega tus Primeros Límites de Tiempo",
            body = "",
            tool = ToolType.LIMIT_DAILY,
            taskPrerrequisitesID = null,
            isMandatory = false,
            readingMinutes = 3
        )
    )

    val doneTasks = listOf(
        Task(
            id = "04",
            title = "¿Qué es la Terapia Condctual?",
            body = "",
            tool = null,
            taskPrerrequisitesID = null,
            isMandatory = false,
            readingMinutes = 5
        ),
        Task(
            id = "05",
            title = "Añade Recordatorios para Descansar del Teléfono",
            body = "",
            tool = ToolType.REST_REMINDERS,
            taskPrerrequisitesID = null,
            isMandatory = false,
            readingMinutes = 5
        )
    )

    ReclaimYourAttentionTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(pendingTasks) { task ->
                    TaskItem(task)
                }
                items(doneTasks) { task ->
                    TaskItem(task, true)
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, areDone: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(DarkGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .fillMaxHeight()  // Llena la altura intrínseca del Row (definida por el contenido de la derecha)
                    .background(
                        color =
                        if (areDone) {
                            Gray
                        } else if (task.tool != null) {
                            MaterialTheme.colorScheme.primary
                        } else if (task.isMandatory) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.tertiary
                        },
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,  // O el icono que prefieras
                    contentDescription = "Task Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = task.title, style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "${task.readingMinutes} ${if (task.readingMinutes == 1) "Minuto" else "Minutos"} de lectura",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}