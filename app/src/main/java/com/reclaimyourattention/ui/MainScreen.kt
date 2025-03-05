package com.reclaimyourattention.ui

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.ui.theme.DarkGray
import com.reclaimyourattention.ui.theme.Gray
import com.reclaimyourattention.viewmodel.PhaseViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.LimitNotifications
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ReclaimYourAttentionTheme {
        MainScreen()
    }
}

// ViewModel para manejar la navegación y generación de TaskScreens
object TaskViewModel : ViewModel() {
    // Estado privado
    private val _selectedTask = MutableStateFlow<Task?>(null)

    // Flujo público (usa asStateFlow para evitar mutaciones externas)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    // Función para actualizar la tarea
    fun selectTask(task: Task) {
        Log.d("VIEWMODEL", "Tarea almacenada: ${task.title}")
        _selectedTask.value = task
    }
}

// Enum para la lógica del filtrado de tareas
enum class FilterType {
    OBLIGATORIAS,
    OPCIONALES,
    HERRAMIENTAS,
    NONE
}

@Composable
fun MainScreen(navController: NavController? = null) {
    // Estados PhaseViewModel
    val currentPhase by PhaseViewModel.currentPhase.observeAsState()
    val currentTasks by PhaseViewModel.currentTasks.observeAsState(initial = emptyList())
    val completedTasks by PhaseViewModel.completedTasks.observeAsState(initial = emptyList())
    val canAdvance by PhaseViewModel.canAdvancePhase.observeAsState(initial = false)

    // Función de filtrado (dentro del composable)
    var selectedFilter by remember { mutableStateOf(FilterType.NONE) }
    fun Task.matchesFilter(filter: FilterType): Boolean {
        return when (filter) {
            FilterType.OBLIGATORIAS -> isMandatory
            FilterType.OPCIONALES -> !isMandatory
            FilterType.HERRAMIENTAS -> tool != null
            FilterType.NONE -> true
        }
    }

    // Listas filtradas
    val filteredCurrentTasks = currentTasks.filter { it.matchesFilter(selectedFilter) }
    val filteredCompletedTasks = completedTasks.filter { it.matchesFilter(selectedFilter) }

    Scaffold(
        floatingActionButton = {
            if (canAdvance) {
                FloatingActionButton(
                    onClick = { PhaseViewModel.onAdvancePhaseClicked() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Avanzar fase")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO("QUITAR TEST")
            Button(
                onClick = {
                    RestReminders.activate(1)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFilter == FilterType.OPCIONALES) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Gray
                    }
                )
            ) {
                Text(
                    text = "Test",
                )
            }

            // Información de la Fase
            currentPhase?.let { phase ->
                Text(
                    text = phase.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Semana ${phase.currentWeekIndex+1}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = phase.description,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de Filtrado
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
                    // Botón Tareas Obligatorias
                    Button(
                        onClick = {
                            selectedFilter = if (selectedFilter == FilterType.OBLIGATORIAS) FilterType.NONE
                            else FilterType.OBLIGATORIAS
                        },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFilter == FilterType.OBLIGATORIAS) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                Gray
                            }
                        )
                    ) {
                        Text(
                            text = "Obligatorias",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Botón Tareas con Herramientas
                    Button(
                        onClick = {
                            selectedFilter = if (selectedFilter == FilterType.HERRAMIENTAS) FilterType.NONE
                            else FilterType.HERRAMIENTAS
                        },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFilter == FilterType.HERRAMIENTAS) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Gray
                            }
                        )
                    ) {
                        Text(
                            text = "Herramientas",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                // Botón Tareas Opcionales
                Button(
                    onClick = {
                        selectedFilter = if (selectedFilter == FilterType.OPCIONALES) FilterType.NONE
                        else FilterType.OPCIONALES
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFilter == FilterType.OPCIONALES) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            Gray
                        }
                    )
                ) {
                    Text(
                        text = "Opcionales",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tareas
            LazyColumn {
                items(filteredCurrentTasks) { task ->
                    TaskItem(
                        task = task,
                        navController = navController
                    )
                }
                items(filteredCompletedTasks) { task ->
                    TaskItem(
                        task = task,
                        areDone = true,
                        navController = navController
                    )
                }
            }
        }
    }
}

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
fun TaskItem(task: Task, areDone: Boolean = false, navController: NavController? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                if (navController != null) {
                    Log.d("NAVIGATION", "Tarea seleccionada: ${task.title}")
                    TaskViewModel.selectTask(task)
                    navController.navigate("task")
                }
            },
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
                    imageVector = if (areDone) {
                        Icons.Default.CheckBox // Tareas completada
                    } else {
                        when {
                            task.tool != null -> Icons.Default.Build // Tarea con herramienta
                            task.isMandatory -> Icons.Default.Warning // Tarea obligatoria sin herramienta
                            else -> Icons.Default.Star // Tarea opcional
                        }
                    },
                    contentDescription = if (areDone) {
                        "Tarea completada"
                    } else {
                        when {
                            task.tool != null -> "Tarea con herramienta"
                            task.isMandatory -> "Tarea obligatoria"
                            else -> "Tarea opcional"
                        }
                    },
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