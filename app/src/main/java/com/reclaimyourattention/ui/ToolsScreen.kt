package com.reclaimyourattention.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reclaimyourattention.OpenAccessibilitySettingsButton
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(modifier: Modifier=Modifier) {
    val context = LocalContext.current
    Scaffold(
        topBar = { TopAppBar(title = { Text("Herramientas") },Modifier.padding(top=40.dp)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BodyContent(RestReminders, WaitTimeForApp)
            BotonLimitTimePerSession(LimitTimePerSession)
            BotonAppBlock(AppBlock)
            ServiceButtons(context)
            }
        Column( modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun previewTool() {
    ReclaimYourAttentionTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Herramientas") }) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BodyContent(RestReminders, WaitTimeForApp)
                BotonLimitTimePerSession(LimitTimePerSession)
                BotonAppBlock(AppBlock)
            }
        }
    }
}

@Composable
fun tool(appName: String, timeUsed: Int) {
    Row {

    }
}