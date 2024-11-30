package com.example.arimagaame.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ArimaaGameBoard() {
    val boardState = remember { mutableStateOf(ArimaaUtils.createInitialBoard()) }
    var selectedPiece by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var currentPlayer by remember { mutableStateOf("Gold Player") }
    var goldPlayerMoves by remember { mutableStateOf(0) }
    var silverPlayerMoves by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMoveType by remember { mutableStateOf<String>("") }
    val history = remember { mutableListOf(boardState.value.map { it.copyOf() }) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val boardSize = minOf(maxWidth, maxHeight)
        val density = LocalDensity.current.density
        val boardSizePx = boardSize.toPx(density)
        val cellSizePx = boardSizePx / 8

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display current player and their moves
            Text(text = "Current Player: $currentPlayer", modifier = Modifier.padding(16.dp))

            // Canvas for the game board
            Surface(
                modifier = Modifier
                    .size(boardSize)
                    .padding(4.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEEEEEE)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val col = (offset.x / cellSizePx).toInt()
                                val row = (offset.y / cellSizePx).toInt()

                                if (selectedPiece == null) {
                                    // Select a piece
                                    if (boardState.value[row][col].startsWith(currentPlayer.first().toString())) {
                                        selectedPiece = row to col
                                    }
                                } else {
                                    // Try to move the selected piece
                                    val (srcRow, srcCol) = selectedPiece!!

                                    if (abs(srcRow - row) + abs(srcCol - col) == 1) {
                                        val piece = boardState.value[srcRow][srcCol]

                                        if (ArimaaUtils.canMovePiece(piece, boardState.value, row, col)) {
                                            // Check if we're allowed to push or pull
                                            val targetPiece = boardState.value[row][col]

                                            if (targetPiece.isNotEmpty() && piece > targetPiece) {
                                                // Check if we are pushing a piece
                                                if (ArimaaUtils.canPushPiece(piece, targetPiece, boardState.value, row, col)) {
                                                    // Push logic: Move both pieces
                                                    ArimaaUtils.pushPiece(piece, targetPiece, boardState.value, srcRow, srcCol, row, col)
                                                    // Two moves for push
                                                    goldPlayerMoves += 2
                                                }
                                            }

                                            // Handle immobility for smaller pieces
                                            if (ArimaaUtils.isImmobile(boardState.value, row, col)) {
                                                errorMessage = "This piece is immobile!"
                                            } else {
                                                boardState.value[row][col] = piece
                                                boardState.value[srcRow][srcCol] = ""
                                                selectedPiece = null
                                                // Increment the move count for the current player
                                                if (currentPlayer == "Gold Player") {
                                                    goldPlayerMoves++
                                                } else {
                                                    silverPlayerMoves++
                                                }

                                                // Handle trap squares
                                                if (ArimaaUtils.isInTrap(boardState.value, row, col, currentPlayer)) {
                                                    boardState.value[row][col] = ""
                                                }

                                                // End game if a rabbit crosses to the other side
                                                if (ArimaaUtils.hasWon(currentPlayer, row)) {
                                                    errorMessage = "$currentPlayer Wins! A rabbit has reached the other side."
                                                }
                                            }

                                            // Switch turns after 4 moves
                                            if ((currentPlayer == "Gold Player" && goldPlayerMoves >= 4) ||
                                                (currentPlayer == "Silver Player" && silverPlayerMoves >= 4)) {
                                                // Switch turns
                                                currentPlayer = if (currentPlayer == "Gold Player") "Silver Player" else "Gold Player"

                                                // Reset move counter for the next player
                                                if (currentPlayer == "Gold Player") {
                                                    goldPlayerMoves = 0
                                                } else {
                                                    silverPlayerMoves = 0
                                                }
                                            }
                                        } else {
                                            errorMessage = "Invalid move: A smaller piece can't move next to a larger piece."
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    drawBoard()
                    drawPieces(boardState.value, selectedPiece)
                }
            }

            // Error message for invalid move
            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            // Reset and Undo buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        boardState.value = ArimaaUtils.createInitialBoard()
                        selectedPiece = null
                        currentPlayer = "Gold Player"
                        goldPlayerMoves = 0
                        silverPlayerMoves = 0
                        history.clear()
                        errorMessage = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Reset Game")
                }

                Button(
                    onClick = {
                        // Undo the last move if there's history
                        if (history.size > 1) {
                            history.removeAt(history.size - 1) // Remove the current state
                            val previousState = history.last()
                            boardState.value = previousState.map { it.copyOf() }.toTypedArray()
                            selectedPiece = null
                            // Revert move count and switch back to the previous player
                            if (currentPlayer == "Gold Player") {
                                goldPlayerMoves--
                            } else {
                                silverPlayerMoves--
                            }
                            currentPlayer = if (currentPlayer == "Gold Player") "Silver Player" else "Gold Player"
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Undo")
                }
            }
        }
    }
}

// Helper to convert Dp to Px within a Density context
fun Dp.toPx(density: Float): Float {
    return this.value * density
}

// Helper to draw the board
fun DrawScope.drawBoard() {
    val cellSize = size.width / 8
    for (row in 0..7) {
        for (col in 0..7) {
            val isTrap = (row == 2 || row == 5) && (col == 2 || col == 5)
            drawRect(
                color = when {
                    isTrap -> Color.Red
                    (row + col) % 2 == 0 -> Color(0xFFDDDDDD)
                    else -> Color(0xFFAAAAAA)
                },
                topLeft = Offset(col * cellSize, row * cellSize),
                size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
            )
        }
    }
}

// Helper to draw the pieces
fun DrawScope.drawPieces(board: Array<Array<String>>, selectedPiece: Pair<Int, Int>?) {
    val cellSize = size.width / 8
    board.forEachIndexed { row, cols ->
        cols.forEachIndexed { col, piece ->
            if (piece.isNotEmpty()) {
                val pieceColor = if (piece.startsWith('G')) Color.Yellow else Color.Gray
                drawCircle(
                    color = if (selectedPiece == row to col) Color.Green else pieceColor,
                    center = Offset(
                        x = col * cellSize + cellSize / 2,
                        y = row * cellSize + cellSize / 2
                    ),
                    radius = cellSize / 4
                )
            }
        }
    }
}
