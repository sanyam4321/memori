package com.example.memori.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Let's give it a "Memory/Brain" color palette for your demo!
private val NeonColorFamily = darkColorScheme(
    primary = NeonPurple,
    secondary = NeonCyan,
    tertiary = NeonPink,
    background = NeonBackground,
    surface = SurfaceDark,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun MemoriTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeonColorFamily,
        content = content
    )
}

