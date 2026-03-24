package com.example.memori.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.memori.viewmodel.MemoriViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    viewModel: MemoriViewModel,
    onFinished: () -> Unit,
    onBackClick: () -> Unit
) {
    val dueCards by viewModel.dueCards.collectAsState()
    var isFlipped by remember { mutableStateOf(false) }

    LaunchedEffect(dueCards.isEmpty()) {
        if (dueCards.isEmpty()) {
            onFinished()
        }
    }

    if (dueCards.isEmpty()) return

    val currentCard = dueCards.first()

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "cardFlipAnimation"
    )

    val isFrontVisible = rotation <= 90f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Session") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "${dueCards.size} cards remaining today",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .graphicsLayer {
                        rotationY = rotation

                        cameraDistance = 12f * density
                    }
                    .clickable { isFlipped = !isFlipped },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFrontVisible)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isFrontVisible) {

                        Text(
                            text = currentCard.frontText,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp)
                        )
                    } else {

                        Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                            Text(
                                text = currentCard.backText,
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            if (isFlipped) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DifficultyButton(
                        text = "Again",
                        color = Color(0xFFE53935),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            isFlipped = false
                            viewModel.processReview(currentCard, 0)
                        }
                    )
                    DifficultyButton(
                        text = "Hard",
                        color = Color(0xFFFB8C00),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            isFlipped = false
                            viewModel.processReview(currentCard, 1)
                        }
                    )
                    DifficultyButton(
                        text = "Good",
                        color = Color(0xFF43A047),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            isFlipped = false
                            viewModel.processReview(currentCard, 2)
                        }
                    )
                    DifficultyButton(
                        text = "Easy",
                        color = Color(0xFF1E88E5),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            isFlipped = false
                            viewModel.processReview(currentCard, 3)
                        }
                    )
                }
            } else {
                Text(
                    text = "Tap the card to reveal the answer",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DifficultyButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}