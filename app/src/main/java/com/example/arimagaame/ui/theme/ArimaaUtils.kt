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
        return history.any { state -> state.contentDeepEquals(boardState) }
    }

    // Check if a piece can move (considering size of pieces and neighboring pieces)
    fun canMovePiece(piece: String, board: Array<Array<String>>, row: Int, col: Int): Boolean {
        val pieceSize = getPieceSize(piece)
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
        return if (piece.startsWith("G")) 1 else 2 // Simplified example: "G" for smaller (Rabbit, Camel), "S" for larger (Horse, Elephant)
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

    // Check if the piece is immobile (if it's next to a larger piece)
    fun isImmobile(board: Array<Array<String>>, row: Int, col: Int): Boolean {
        val piece = board[row][col]
        val pieceSize = getPieceSize(piece)

        val neighbors = getNeighbors(row, col)
        for (neighbor in neighbors) {
            val (r, c) = neighbor
            val neighborPiece = board[r][c]
            if (neighborPiece.isNotEmpty() && getPieceSize(neighborPiece) > pieceSize) {
                return true
            }
        }
        return false
    }

    // Check if a piece is in a trap
    fun isInTrap(board: Array<Array<String>>, row: Int, col: Int, player: String): Boolean {
        val isTrap = (row == 2 || row == 5) && (col == 2 || col == 5)
        if (isTrap) {
            // Remove the piece if it's in a trap unless a friendly piece is adjacent
            val neighbors = getNeighbors(row, col)
            for (neighbor in neighbors) {
                val (r, c) = neighbor
                if (board[r][c].startsWith(player)) {
                    return false // Friendly piece is adjacent, don't remove
                }
            }
            return true // Trap, remove piece
        }
        return false
    }

    // Check if a rabbit has reached the other side
    fun hasWon(player: String, row: Int): Boolean {
        return if (player == "Gold Player") row == 0 else row == 7
    }

    // Can a larger piece push a smaller piece
    fun canPushPiece(
        piece: String,
        targetPiece: String,
        board: Array<Array<String>>,
        row: Int,
        col: Int
    ): Boolean {
        return getPieceSize(piece) > getPieceSize(targetPiece) && board[row][col].isEmpty()
    }

    // Push a smaller piece with a larger piece (move both)
    fun pushPiece(
        piece: String,
        targetPiece: String,
        board: Array<Array<String>>,
        srcRow: Int,
        srcCol: Int,
        row: Int,
        col: Int
    ) {
        // Move the larger piece first
        board[row][col] = piece
        board[srcRow][srcCol] = ""

        // Move the smaller piece in the same direction
        val (pushRow, pushCol) = getPushDirection(srcRow, srcCol, row, col)
        board[pushRow][pushCol] = targetPiece
        board[row][col] = piece
    }

    // Get direction for pushing a piece (simply moving one step)
    private fun getPushDirection(srcRow: Int, srcCol: Int, row: Int, col: Int): Pair<Int, Int> {
        return when {
            srcRow < row -> row - 1 to col
            srcRow > row -> row + 1 to col
            srcCol < col -> row to col - 1
            else -> row to col + 1
        }
    }

    // Pulling a smaller piece with a larger piece (counts as two moves)
    fun canPullPiece(piece: String, targetPiece: String, board: Array<Array<String>>, row: Int, col: Int): Boolean {
        return getPieceSize(piece) > getPieceSize(targetPiece) && board[row][col].isEmpty()
    }

    // Pull the piece
    fun pullPiece(piece: String, targetPiece: String, board: Array<Array<String>>, srcRow: Int, srcCol: Int, row: Int, col: Int) {
        // Move the larger piece
        board[row][col] = piece
        board[srcRow][srcCol] = ""

        // Move the smaller piece towards the larger one
        val (pullRow, pullCol) = getPullDirection(srcRow, srcCol, row, col)
        board[pullRow][pullCol] = targetPiece
    }

    // Get direction for pulling a piece
    private fun getPullDirection(srcRow: Int, srcCol: Int, row: Int, col: Int): Pair<Int, Int> {
        return when {
            srcRow < row -> row + 1 to col
            srcRow > row -> row - 1 to col
            srcCol < col -> row to col + 1
            else -> row to col - 1
        }
    }
}
