object ArimaaUtils {

    // Create an initial board
    fun createInitialBoard(): Array<Array<String>> {
        val board = Array(8) { Array(8) { "" } }

        // Gold pieces
        board[7] = arrayOf("G-R", "G-R", "G-R", "G-R", "G-R", "G-R", "G-R", "G-R")
        board[6] = arrayOf("G-C", "G-D", "G-H", "G-M", "G-E", "G-H", "G-D", "G-C")

        // Silver pieces
        board[0] = arrayOf("S-C", "S-D", "S-H", "S-M", "S-E", "S-H", "S-D", "S-C")
        board[1] = arrayOf("S-R", "S-R", "S-R", "S-R", "S-R", "S-R", "S-R", "S-R")

        return board
    }
    // Check if the state has been seen before
    fun hasSeenStateBefore(history: List<Array<Array<String>>>, boardState: Array<Array<String>>): Boolean {
        return history.any { state ->
            state.contentDeepEquals(boardState)
        }
    }

    // Check if a piece can move (considering size of pieces and neighboring pieces)
    fun canMovePiece(piece: String, board: Array<Array<String>>, row: Int, col: Int): Boolean {
        val pieceSize = if (piece.startsWith("G")) 1 else 2 // Example: "G" for smaller, "S" for larger
        val neighbors = getNeighbors(row, col)

        for (neighbor in neighbors) {
            val (r, c) = neighbor
            val neighborPiece = board[r][c]
            if (neighborPiece.isNotEmpty() && neighborPiece != piece && getPieceSize(neighborPiece) > pieceSize) {
                return false // Can't move if a larger piece is nearby
            }
        }
        return true
    }

    // Define the size of the piece based on its letter
    private fun getPieceSize(piece: String): Int {
        return if (piece.startsWith("G")) 1 else 2 // Simplified example logic
    }

    // Get neighboring cells
    private fun getNeighbors(row: Int, col: Int): List<Pair<Int, Int>> {
        val neighbors = mutableListOf<Pair<Int, Int>>()
        if (row > 0) neighbors.add(row - 1 to col)
        if (row < 7) neighbors.add(row + 1 to col)
        if (col > 0) neighbors.add(row to col - 1)
        if (col < 7) neighbors.add(row to col + 1)
        return neighbors
    }
}
