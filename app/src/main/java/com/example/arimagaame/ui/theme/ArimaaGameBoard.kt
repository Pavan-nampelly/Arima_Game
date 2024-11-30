package com.example.arimagaame.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arimagaame.ui.theme.ArimaaUtils.createInitialBoard

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ArimaaGameBoard() {
    // Define the board state
    val boardState = remember { mutableStateOf(createInitialBoard()) }
    // Ensure the composable remains square
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val boardSize = minOf(maxWidth, maxHeight)
        Surface(
            modifier = Modifier
                .size(boardSize)
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFEEEEEE)
        ) {
            // Draw the 8x8 board
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawBoard()
                drawPieces(boardState.value)
            }
        }
    }
}

// Helper to draw the board
fun DrawScope.drawBoard() {
    val cellSize = size.width / 8
    for (row in 0..7) {
        for (col in 0..7) {
            val isTrap = (row == 2 || row == 5) && (col == 2 || col == 5)
            drawRect(
                color = if (isTrap) Color.Red else if ((row + col) % 2 == 0) Color(0xFFDDDDDD) else Color(0xFFAAAAAA),
                topLeft = androidx.compose.ui.geometry.Offset(col * cellSize, row * cellSize),
                size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
            )
        }
    }
}

// Helper to draw the pieces
fun DrawScope.drawPieces(board: Array<Array<String>>) {
    val cellSize = size.width / 8
    board.forEachIndexed { row, cols ->
        cols.forEachIndexed { col, piece ->
            if (piece.isNotEmpty()) {
                val pieceColor = if (piece.first() == 'G') Color.Yellow else Color.Gray
                drawCircle(
                    color = pieceColor,
                    center = androidx.compose.ui.geometry.Offset(
                        x = col * cellSize + cellSize / 2,
                        y = row * cellSize + cellSize / 2
                    ),
                    radius = cellSize / 4
                )
            }
        }
    }
}
