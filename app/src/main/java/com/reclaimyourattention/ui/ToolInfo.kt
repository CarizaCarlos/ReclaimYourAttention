package com.reclaimyourattention.ui

import android.content.ContentResolver
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.logic.tools.AppInfo
import com.reclaimyourattention.logic.tools.LimitNotifications
import com.reclaimyourattention.logic.tools.LimitTimeInApp
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.Tool
import com.reclaimyourattention.ui.ToolsScreens.AppBlockViewModel
import com.reclaimyourattention.ui.ToolsScreens.AppBlockViewModelFactory
import com.reclaimyourattention.ui.theme.Gray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
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

            // Parámetros y Botón de Activación Específicos de cada Tool
            when (tool) {
                is RestReminders -> GetRestRemindersParameters()
                is LimitNotifications -> GetLimitNotifications()
                is LimitTimeInApp -> GetLimitTimeInApp()
            }

            Button(
                onClick = { tool.deactivate() }
            ) {
                Text("Desactivar")
            }
        }
    }
}

@Composable
fun GetLimitNotifications() {
    var blockedPackages = LimitNotifications.blockedPackages
    GetBlockedPackages(
        initialBlockedPackages = blockedPackages, // Envía el estado actual
        onBlockedSelected = { newSelection ->
            blockedPackages =
                newSelection.toMutableSet() // Actualiza el estado padre
        }
    )

    Button(
        onClick = { LimitNotifications.activate(blockedPackages) }
    ) {
        Text("Activar")
    }
}

@Composable
fun GetBlockedPackages(
    initialBlockedPackages: Set<String> = emptySet(), // Valor inicial
    onBlockedSelected: (Set<String>) -> Unit // Callback para el botón
) {
    val context = LocalContext.current
    val viewModel: AppBlockViewModel = viewModel(
        factory = AppBlockViewModelFactory(context, initialBlockedPackages)
    )

    val installedApps: List<AppInfo> = viewModel.installedApps.collectAsState(initial = emptyList()).value
    val blockedPackages by viewModel.blockedPackages

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Apps Bloqueadas:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .heightIn(max = 100.dp)
        ) {
            items(blockedPackages.toList()) { pkg ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        text = installedApps.firstOrNull { it.packageName == pkg }?.name ?: pkg,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            viewModel.toggleBlock(pkg)
                            onBlockedSelected(viewModel.blockedPackages.value)
                        }
                    ) {
                        Icon(Icons.Default.Delete, "Desbloquear")
                    }
                }
            }
        }

        Text(
            text = "Añadir Apps:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .heightIn(max = 100.dp)
        ) {
            items(installedApps) { app ->
                // Si no está bloqueada, la muestra
                if (!blockedPackages.contains(app.packageName)) {
                    AppListItem(
                        app = app,
                        isBlocked = blockedPackages.contains(app.packageName),
                        onToggle = {
                            viewModel.toggleBlock(app.packageName)
                            onBlockedSelected(viewModel.blockedPackages.value)
                        }
                    )
                }
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
        Spacer(Modifier.heightIn(26.dp))
    // Formatear datos y botón
        var param1 = activityMinutesThreshold.toInt()
        Button(
            onClick = {if(activityMinutesThreshold.matches(Regex("[\\d,.]+")))
                RestReminders.activate(param1)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)){
            Text(
                text = "Enviar",
                style = MaterialTheme.typography.labelLarge
            )
        }
}




@Composable
fun AppListItem(app: AppInfo, isBlocked: Boolean, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = if (isBlocked) Icons.Filled.Block else Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = if (isBlocked) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary
        )
        Text(app.name)
    }
}


@Composable
fun GetLimitTimeInApp(){
    var maxTotalMinutes by remember { mutableStateOf("0") }
    val parsedInt = remember { mutableIntStateOf(0) }
    var maxForEachMinutes by remember { mutableStateOf("0") }
    val parsedInt2 = remember { mutableIntStateOf(0) }
    val defaultValue = 0

    TextField(
        value = maxTotalMinutes,
        onValueChange =
        { newValue ->
            if (newValue.matches(Regex("[\\d,.]+"))) { // Solo permite dígitos numéricos
                maxTotalMinutes = newValue

                parsedInt.intValue = if (newValue.isEmpty()) 0 else newValue.toInt()
                if (parsedInt.intValue == defaultValue && newValue.isNotEmpty()) {
                    maxTotalMinutes = ""
                }
            }
        },
        label = { Text("Label") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("0") } // Placeholder para indicar el valor esperado
    )
    Spacer(Modifier.heightIn(26.dp))

    TextField(
        value = maxForEachMinutes,
        onValueChange =
        { newValue ->
            if (newValue.matches(Regex("[\\d,.]+"))) {
                maxForEachMinutes = newValue

                parsedInt2.intValue = if (newValue.isEmpty()) 0 else newValue.toInt()
                if (parsedInt2.intValue == defaultValue && newValue.isNotEmpty()) {
                    maxForEachMinutes = ""
                }
            }
        },
        label = { Text("Label") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("0") } // Placeholder para indicar el valor esperado
    )

    //Falta boton para enviar Info y el parametro blockedPackages
}

@Composable
fun GetLimitTimePerSession(){
    var activeMinutesThreshold by remember { mutableStateOf("0") }
    val parsedInt = remember { mutableIntStateOf(0) } // Variable donde se almacenará el entero
    var cooldownMinutes by remember { mutableStateOf("0") }
    val parsedInt2 = remember { mutableIntStateOf(0) } // Variable donde se almacenará el entero

    val defaultValue = 0
    TextField(
        value = activeMinutesThreshold,
        onValueChange =
        { newValue ->
            if (newValue.matches(Regex("[\\d,.]+"))) { // Solo permite dígitos numéricos
                activeMinutesThreshold = newValue

                parsedInt.intValue = if (newValue.isEmpty()) 0 else newValue.toInt()
                if (parsedInt.intValue == defaultValue && newValue.isNotEmpty()) {
                    activeMinutesThreshold = ""

                }
            }
        }, // Verificar que sea un numero

        label = { Text("Label") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("0") } // Placeholder para indicar el valor esperado
    )
    Spacer(Modifier.heightIn(36.dp))
    TextField(
        value = cooldownMinutes,
        onValueChange =
        { newValue ->
            if (newValue.matches(Regex("[\\d,.]+"))) { // Solo permite dígitos numéricos
                cooldownMinutes = newValue

                parsedInt2.intValue = if (newValue.isEmpty()) 0 else newValue.toInt()
                if (parsedInt2.intValue == defaultValue && newValue.isNotEmpty()) {
                    cooldownMinutes = ""

                }
            }
        },

        label = { Text("Label") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("0") } // Placeholder para indicar el valor esperado
    )

    }