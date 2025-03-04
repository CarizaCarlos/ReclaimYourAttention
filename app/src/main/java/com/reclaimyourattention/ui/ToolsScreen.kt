package com.reclaimyourattention.ui

import android.content.Context
import android.content.Intent
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
import com.reclaimyourattention.OpenAccessibilitySettingsButton
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.BlockingScheduleForApp
import com.reclaimyourattention.logic.tools.LimitNotifications
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.Tool
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.ui.theme.DarkGray
import com.reclaimyourattention.ui.theme.Gray
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme


@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ToolsScreen(navController: NavController? = null) {
    val context = LocalContext.current
    Scaffold(
        topBar = { TopAppBar(title = { Text("Herramientas") },Modifier.padding(top=40.dp)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Información de herramienta
            ToolItem(AppBlock)
            ToolItem(LimitTimePerSession)
            ToolItem(RestReminders)
            ToolItem(BlockingScheduleForApp)
            ToolItem(LimitNotifications)
            //BotonLimitTimePerSession(LimitTimePerSession)
            //BotonAppBlock(AppBlock)
            //ServiceButtons(context)
            }

    }
}



/*
// Funciones de ToolsScreen
@Composable
fun BodyContent(
    r: RestReminders,
    w: WaitTimeForApp,
){
        //RestReminder

        // Estado para rastrear si está activado
        var isReminderActive by remember { mutableStateOf(false) }
        Button(onClick = {
            if (isReminderActive) {
                r.deactivate() // Llamar a la función de desactivación
            } else {
                r.activate(1) // Llamar a la función de activación
            }
            isReminderActive = !isReminderActive // Cambiar el estado
        }) {
            Text(if (isReminderActive) "Desactivar RestReminders" else "Activar RestReminders")
        }

        // Wait Time
        var isWaitTimeActive by remember { mutableStateOf(false) }
        Button(onClick = {
            val waitSeconds = 5
            val blockedPackages: MutableSet<String> = mutableSetOf("com.android.chrome")

            if (isWaitTimeActive) {
                w.deactivate() // Llamar a la función de desactivación
            } else {
                w.activate(waitSeconds, blockedPackages) // Llamar a la función de activación
            }
            isWaitTimeActive = !isWaitTimeActive // Cambiar el estado
        }) {
            Text(if (isWaitTimeActive) "Desactivar WaitTimeForApp" else "Activar WaitTimeForApp")
        }
}
@Composable
fun BotonLimitTimePerSession(s: LimitTimePerSession) {
    // Time Sesh
    var isSeshActive by remember { mutableStateOf(false) } // Estado para rastrear si está activado
    Button(onClick = {
        val activeMinutesThreshold = 1
        val cooldownMinutes = 1
        val blockedPackages: MutableSet<String> = mutableSetOf(
            "com.android.chrome",
            "com.google.android.youtube"
        )

        if (isSeshActive) {
            s.deactivate() // Desactiva si ya está activo
        } else {
            s.activate(
                activeMinutesThreshold,
                cooldownMinutes,
                blockedPackages
            ) // Activa si está inactivo
        }
        isSeshActive = !isSeshActive // Cambiar el estado
    }) {
        Text(if (isSeshActive) "Desactivar LimitTimePerSession" else "Activar LimitTimePerSession")
    }
}
@Composable
fun BotonAppBlock(b: AppBlock){
    // AppBlock
    var isAppBlockActive by remember { mutableStateOf(false) } // Estado para rastrear si está activado

    Button(onClick = {
        val blockedPackages = mutableSetOf("com.google.android.apps.messaging")

        if (isAppBlockActive) {
            b.deactivate() // Desactiva si ya está activo
        } else {
            b.activate(blockedPackages) // Activa si está inactivo
        }
        isAppBlockActive = !isAppBlockActive // Cambia el estado
    }) {
        Text(if (isAppBlockActive) "Desactivar AppBlock" else "Activar AppBlock")
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
*/

@Composable
fun ToolItem(tool: Tool, areDone: Boolean = false, navController: NavController? = null, onClick: (() -> Unit?)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                if (onClick != null) {
                    onClick()
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


