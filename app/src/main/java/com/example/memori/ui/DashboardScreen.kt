package com.example.memori.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.memori.data.ReviewLog
import com.example.memori.viewmodel.MemoriViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MemoriViewModel,
    onStartStudy: () -> Unit,
    onManageCards: () -> Unit,
    onSearchClick: () -> Unit
) {
    val dueCards by viewModel.dueCards.collectAsState()
    val allCards by viewModel.allCards.collectAsState()
    val recentLogs by viewModel.recentLogs.collectAsState()

    val csvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.importCsv(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Memori Dashboard", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = { onSearchClick() }) {
                        Icon(Icons.Rounded.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()), // Enables scrolling on small screens
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Stats Section
            StatCards(
                totalCards = allCards.size,
                dueCards = dueCards.size
            )

            ActivitySection(recentLogs = recentLogs)

            ActionButtons(
                isStudyDue = dueCards.isNotEmpty(),
                onStartStudy = onStartStudy,
                onManageCards = onManageCards,
                onImportCsv = { csvPickerLauncher.launch("*/*") }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatCards(totalCards: Int, dueCards: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Due Today",
            value = dueCards.toString(),
            icon = Icons.Rounded.Notifications,
            isHighlight = dueCards > 0
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Total Cards",
            value = totalCards.toString(),
            icon = Icons.Rounded.Style,
            isHighlight = false
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    isHighlight: Boolean
) {
    val containerColor = if (isHighlight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isHighlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        }
    }
}

@Composable
private fun ActivitySection(recentLogs: List<ReviewLog>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Your Activity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                HeatmapComponent(logs = recentLogs)
            }
        }
    }
}

@Composable
private fun ActionButtons(
    isStudyDue: Boolean,
    onStartStudy: () -> Unit,
    onManageCards: () -> Unit,
    onImportCsv: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Primary Action
        Button(
            onClick = onStartStudy,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isStudyDue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = if (isStudyDue) Icons.Rounded.PlayArrow else Icons.Rounded.CheckCircle,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = if (isStudyDue) "Start Studying" else "You're all caught up!",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalButton(
                onClick = onManageCards,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp).size(18.dp)
                )
                Text("Manage")
            }

            OutlinedButton(
                onClick = onImportCsv,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Download,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp).size(18.dp)
                )
                Text("Import")
            }
        }
    }
}