package com.example.memori.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.memori.viewmodel.MemoriViewModel


@Composable
fun DashboardScreen(viewModel: MemoriViewModel, onStartStudy: () -> Unit, onManageCards: () -> Unit) {
    val dueCards by viewModel.dueCards.collectAsState()
    val allCards by viewModel.allCards.collectAsState()
    val recentLogs by viewModel.recentLogs.collectAsState()



    val csvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.importCsv(it) }
    }


    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Memori Dashboard", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))


        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Cards: ${allCards.size}", style = MaterialTheme.typography.bodyLarge)
                Text("Cards Due Today: ${dueCards.size}", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        HeatmapComponent(logs = recentLogs)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartStudy,
            modifier = Modifier.fillMaxWidth(),

        ) {
            Text(if (dueCards.isNotEmpty()) "Start Studying" else "You're all caught up!")
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onManageCards,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Flashcards")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {

                csvPickerLauncher.launch("*/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import CSV Presentation Data")
        }
    }
}