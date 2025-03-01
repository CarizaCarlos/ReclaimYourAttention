package com.reclaimyourattention.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.reclaimyourattention.logic.services.BlockRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAppScreen(request: BlockRequest) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Blocked App") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Mensaje
            Text(request.message)

            // Countdown (Si lo requiere)
            //if (request.showCountdown) {
                //request.unblockTime?.let { CountdownTimer(it) }
            //}
        }
    }
}
