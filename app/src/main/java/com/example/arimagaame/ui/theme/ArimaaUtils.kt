package com.example.arimagaame.ui.theme

object ArimaaUtils {
    fun createInitialBoard(): Array<Array<String>> {
        val board = Array(8) { Array(8) { "" } }

//        // Gold pieces
//        board[7] = arrayOf("G-R", "G-R", "G-R", "G-R", "G-R", "G-R", "G-R", "G-R")
//        board[6] = arrayOf("G-C", "G-D", "G-H", "G-M", "G-E", "G-H", "G-D", "G-C")

        // Silver pieces
        board[0] = arrayOf("S-C", "S-D", "S-H", "S-M", "S-E", "S-H", "S-D", "S-C")
        board[1] = arrayOf("S-R", "S-R", "S-R", "S-R", "S-R", "S-R", "S-R", "S-R")

        return board
    }
}
