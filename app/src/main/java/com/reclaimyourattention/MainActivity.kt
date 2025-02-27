package com.reclaimyourattention

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.tools.AppBlock
import com.reclaimyourattention.logic.tools.LimitTimePerSession
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.ToolsScreen
import com.reclaimyourattention.ui.UsageScreen
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme

import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.datetime.format.Padding


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val r = RestReminders(this)
        val w = WaitTimeForApp(this)
        val s = LimitTimePerSession(this)
        val b = AppBlock(this)

        val context = this

        setContent {
            ReclaimYourAttentionTheme {
                Naveg()
                Column (modifier = Modifier.fillMaxWidth().padding(top=100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,)
                {
                    BodyContent(r,w)
                    BotonAppBlock(b)
                    BotonLimitTimePerSession(s)
                    // AppBlock Service
                    Button(onClick = {
                        context.startService(Intent(context, AppBlockService::class.java))
                    }) {
                        Text("Activar AppBlockService")
                    }

                    // Accesibility Service Activation
                    OpenAccessibilitySettingsButton(context)
                }

                // Permiso pa mostrar sobre otras apps
                requestOverlayPermission(this)
            }
        }
    }
}


@Composable
fun Naveg(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("tools") { ToolsScreen(navController) }
        composable("usage") { UsageScreen(navController) }
        //RestReminder
    }
}


@Composable
fun BodyContent(
    r: RestReminders,
    w: WaitTimeForApp,
    ){
    Column {
        //RestReminder

        // Estado para rastrear si está activado
        var isReminderActive by remember { mutableStateOf(false) }
        Button(onClick = {
            if (isReminderActive) {
                r.deactivate() // Llamar a la función de desactivación
            } else {
                r.activate() // Llamar a la función de activación
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier

    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    //A
    val r = RestReminders(context)
    val w = WaitTimeForApp(context)
    val s = LimitTimePerSession(context)
    val b = AppBlock(context)
    ReclaimYourAttentionTheme {
        Naveg()
        Column(modifier = Modifier.fillMaxWidth().padding(top=90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            BodyContent(r, w)
            BotonAppBlock(b)
            BotonLimitTimePerSession(s)

        }
    }
}

fun requestOverlayPermission(context: Context) {
    if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        context.startActivity(intent)
    }
}

@Composable
fun OpenAccessibilitySettingsButton(context: Context) {
    Button(onClick = { openAccessibilitySettings(context) }) {
        Text("Activar servicio de accesibilidad")
    }
}

fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}