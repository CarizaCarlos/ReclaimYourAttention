package com.reclaimyourattention.ui

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
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reclaimyourattention.R
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Uso") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate("main") }) {
                Text("Ir a Inicio")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("tools") }) {
                Text("Ir a Herramientas")
            }

            // Info específica de Apps
            appInfo("Youtube", 25)
            appInfo("Instagram", 55)
        }
    }
}

@Composable
@Preview
fun previewAppInfo() {
    ReclaimYourAttentionTheme {
        appInfo("App Name", 60)
    }
}

@Composable
fun appInfo(appName: String, timeUsed: Int) {
    Row {
        Image(
            painterResource(R.drawable.ic_launcher_foreground),
            ""
        )
        Column {
            Row {
                Text(appName)
                Text("$timeUsed min")
            }
            progressBar(30)
        }

    }
}

@Composable
fun progressBar(percentage: Byte) {
    Text(".......................$percentage%")
}