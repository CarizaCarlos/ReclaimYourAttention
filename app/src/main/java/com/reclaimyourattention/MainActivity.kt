package com.reclaimyourattention

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reclaimyourattention.logic.services.AppBlockService
import com.reclaimyourattention.logic.services.WaitTimeForAppService
import com.reclaimyourattention.logic.tools.RestReminders
import com.reclaimyourattention.logic.tools.WaitTimeForApp
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.ToolsScreen
import com.reclaimyourattention.ui.UsageScreen
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val r: RestReminders = RestReminders(this)
        val w: WaitTimeForApp = WaitTimeForApp(this)
        val context = this

        setContent {
            ReclaimYourAttentionTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("tools") { ToolsScreen(navController) }
                    composable("usage") { UsageScreen(navController) }
                }
                Column {
                    Button(onClick = {
                        r.activate(1)
                    }) {
                        Text("Activar RestRemidners")
                    }
                    Button(onClick = {
                        r.deactivate()
                    }) {
                        Text("Desactivar RestRemidners")
                    }
                    Button(onClick = {
                        val waitSeconds: Int = 10
                        val blockedPackages: MutableSet<String> = mutableSetOf("com.android.chrome","com.google.android.youtube")
                        w.activate(waitSeconds, blockedPackages)
                    }) {
                        Text("Activar WaitTimeForApp")
                    }
                    Button(onClick = {
                        w.deactivate()
                    }) {
                        Text("Desactivar WaitTimeForApp")
                    }
                    Button(onClick = {
                        context.startService(Intent(context, AppBlockService::class.java))
                    }) {
                        Text("Activar AppBlockService")
                    }
                }
                // Permiso pa mostrar sobre otras apps
                requestOverlayPermission(this)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReclaimYourAttentionTheme {
        Greeting("Android")
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