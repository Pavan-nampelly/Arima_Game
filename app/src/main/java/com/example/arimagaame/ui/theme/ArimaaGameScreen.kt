package com.example.arimagaame.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArimaaGameScreen() {
    // Background gradient with a shade effect
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color.Yellow, // Dark blue shade
            Color.Green  // Lighter blue shade
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush) // Apply gradient background here
    ) {
        // Taskbar with a solid background color
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Arimaa Game",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        // Game Board section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ArimaaGameBoard() // This is your existing board composable
        }
    }
}
