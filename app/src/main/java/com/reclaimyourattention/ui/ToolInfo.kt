package com.reclaimyourattention.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.logic.tools.LimitNotifications
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.Tool
import com.reclaimyourattention.ui.theme.Gray


@Composable
fun ToolScreen(navController: NavController?=null){
    // Recupera la tool
    val tool by ToolViewModel.selectedTool.collectAsState()

    tool?.let { ToolContent(it, navController) }
}

@Composable
fun ToolContent(tool: Tool, navController: NavController?){
    Scaffold ()
    { paddingValues ->
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

            //Titulo y descripción
            Text(
                text = tool.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = tool.description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(36.dp))
        when (tool) {
            is RestReminders -> GetRestRemindersParameters()
            is LimitNotifications -> getLimitNotifications()
        }
        }
    }
}
@Composable
fun GetRestRemindersParameters() {
    // Ingreso de datos
    var activityMinutesThreshold by remember { mutableStateOf("0") }
    val parsedInt = remember { mutableIntStateOf(0) } // Variable donde se almacenará el entero
    val defaultValue = 0
    TextField(
        value = activityMinutesThreshold,
        onValueChange =
        { newValue ->
            if (newValue.matches(Regex("[\\d,.]+"))) { // Solo permite dígitos numéricos
                activityMinutesThreshold = newValue

                parsedInt.intValue = if (newValue.isEmpty()) 0 else newValue.toInt()
                if (parsedInt.intValue == defaultValue && newValue.isNotEmpty()) {
                    activityMinutesThreshold = ""

                }
            }
        }, // Verificar que sea un numero

        label = { Text("Label") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("0") } // Placeholder para indicar el valor esperado
    )

    // Formatear datos
    if(activityMinutesThreshold.matches(Regex("[\\d,.]+"))){
        var param1 = activityMinutesThreshold.toInt()
        RestReminders.activate(param1) // esto va en el onclick del boton
    }
    // Botón
}

fun getLimitNotifications() {}