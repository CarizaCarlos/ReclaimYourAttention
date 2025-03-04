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
fun UsageScreen(navController: NavController?=null) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Uso") },Modifier.padding(top=40.dp)) }
    ) { paddingValues ->
        Column(modifier=Modifier.padding(top=200.dp).padding(horizontal = 50.dp)
        ) {
            appInfo("Youtube", 25)
            appInfo("Instagram", 55)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        }
    }
}

@Composable
@Preview(showSystemUi = true )
fun previewAppInfo() {
    ReclaimYourAttentionTheme {
        Column(
            modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            appInfo("Youtube", 25)
            appInfo("Instagram", 55)
        }
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
                Text(" $timeUsed min")
            }
            progressBar(30)
        }

    }
}

@Composable
fun progressBar(percentage: Byte) {
    Text(".......................$percentage%")
}