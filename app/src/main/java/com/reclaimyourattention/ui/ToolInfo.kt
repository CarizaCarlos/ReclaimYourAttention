package com.reclaimyourattention.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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

            //
//            when {
//                tool instanceof RestReminders -> getParametersRestReminders(),
//                toodl st
//            }
        }
    }
}

fun getParametersRestReminders() {

}