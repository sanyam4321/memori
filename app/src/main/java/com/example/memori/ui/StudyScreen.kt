package com.example.memori.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.memori.viewmodel.MemoriViewModel
import androidx.compose.ui.graphics.Color

@Composable
fun StudyScreen(viewModel: MemoriViewModel, onFinished: () -> Unit) {
    val dueCards by viewModel.dueCards.collectAsState()
    var isFlipped by remember { mutableStateOf(false) }

    if (dueCards.isEmpty()) {
        onFinished()
        return
    }

    val currentCard = dueCards.first()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable { isFlipped = !isFlipped },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = if (isFlipped) currentCard.backText else currentCard.frontText,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isFlipped) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {

                Button(
                    onClick = { viewModel.processReview(currentCard, 0); isFlipped = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text("Again", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = { viewModel.processReview(currentCard, 1); isFlipped = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text("Hard", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = { viewModel.processReview(currentCard, 2); isFlipped = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text("Good", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = { viewModel.processReview(currentCard, 3); isFlipped = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text("Easy", style = MaterialTheme.typography.labelSmall)
                }
            }
        } else {
            Text("Tap the card to reveal the answer")
        }
    }
}