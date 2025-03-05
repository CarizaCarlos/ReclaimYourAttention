package com.reclaimyourattention.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.reclaimyourattention.OpenAccessibilitySettingsButton
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.LimitNotifications
import com.reclaimyourattention.logic.tools.LimitTimeInApp
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.Tool
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.ui.theme.DarkGray
import com.reclaimyourattention.ui.theme.Gray
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//Awo

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ToolsScreen(navController: NavController? = null) {
    val context = LocalContext.current
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Información herramientas
            Text(
                text= "Herramientas",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text="Consulte las utilidades de las diferentes herramientas así como sus estadísticas.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            Spacer(Modifier.height(25.dp))

            ToolItem(AppBlock,navController)
            ToolItem(LimitNotifications,navController)
            ToolItem(LimitTimeInApp,navController)
            ToolItem(LimitTimePerSession,navController)
            ToolItem(RestReminders,navController)

            }

    }
}

// ViewModel para manejar la navegación y generación de TaskScreens
object ToolViewModel : ViewModel() {
    // Estado privado
    private val _selectedTool = MutableStateFlow<Tool?>(null)

    // Flujo público (usa asStateFlow para evitar mutaciones externas)
    val selectedTool: StateFlow<Tool?> = _selectedTool.asStateFlow()

    // Función para actualizar la tarea
    fun selectTool(tool: Tool) {
        Log.d("VIEWMODEL", "Tarea almacenada: ${tool.title}")
        _selectedTool.value = tool
    }
}


@Composable
fun ToolItem(tool: Tool, navController: NavController? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                if (navController != null) {
                    ToolViewModel.selectTool(tool)
                    navController.navigate("toolInfo")
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
                        color = Gray,
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
                    modifier = Modifier.padding(10.dp),
                    text = tool.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,

                    )
            }
        }
    }
}


