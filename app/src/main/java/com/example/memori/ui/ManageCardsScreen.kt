package com.example.memori.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.memori.viewmodel.MemoriViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCardsScreen(viewModel: MemoriViewModel) {
    val allCards by viewModel.allCards.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            items(allCards) { card ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Q: ${card.frontText}", style = MaterialTheme.typography.titleMedium)
                            Text("A: ${card.backText}", style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { viewModel.deleteCard(card) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            var front by remember { mutableStateOf("") }
            var back by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New Flashcard") },
                text = {
                    Column {
                        OutlinedTextField(value = front, onValueChange = { front = it }, label = { Text("Front (Question)") })
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = back, onValueChange = { back = it }, label = { Text("Back (Answer)") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (front.isNotBlank() && back.isNotBlank()) {
                            viewModel.addNewCard(front, back)
                            showAddDialog = false
                        }
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}