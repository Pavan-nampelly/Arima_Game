package com.example.arimagaame.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArimaaGameScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Taskbar
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

        // Game Board
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ArimaaGameBoard()
        }
    }
}
