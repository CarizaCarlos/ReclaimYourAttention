package com.reclaimyourattention.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaimyourattention.R
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import com.reclaimyourattention.viewmodel.PhaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier=Modifier) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio") },Modifier.padding(top=60.dp)) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            PhaseScreen()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

        }
    }
}


//Funciones MainScreen

@Composable
fun PhaseScreen(phaseViewModel: PhaseViewModel = viewModel()) {
    val currentPhase by phaseViewModel.currentPhase.observeAsState()
    val currentTasks by phaseViewModel.currentTasks.observeAsState(initial = emptyList())
    val canAdvance by phaseViewModel.canAdvancePhase.observeAsState(initial = false)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            currentPhase?.let { phase ->
                Text(text = phase.title, style = MaterialTheme.typography.headlineMedium)
                Text(text = phase.description, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Current week: ${phase.currentWeekIndex}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(currentTasks) { task ->
                        TaskItem(task = task) {
                            phaseViewModel.completeTask(task.id)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { phaseViewModel.onAdvancePhaseClicked() },
                    enabled = canAdvance,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Advance Phase")
                }
            } ?: Text(
                text = "All phases completed!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, onComplete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = task.body, style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = onComplete) {
                Text("Mark Completed")
            }
        }
    }
}

//Preview
@Preview(showSystemUi = true)
@Composable
fun previewTask() {
    ReclaimYourAttentionTheme {
        task("App Name", 60)
    }
}

@Composable
fun task(appName: String, timeUsed: Int) {
    Row {
        Image(
            painterResource(R.drawable.ic_launcher_foreground),
            ""
        )
    }
}