package zikrulla.production.chessapp.model

import zikrulla.production.chessapp.R
import kotlin.math.abs

object ChessGame : ChessDelegate {

    private var piecesBox = mutableSetOf<ChessPiece>()

    init {
        reset()
    }

    override fun movePiece(from: Square, to: Square) {
        if (canMove(from, to))
            movePiecePrivate(from, to)
    }

    private fun movePiecePrivate(from: Square, to: Square) {
        if (from.row == to.row && from.col == to.col) return
        if (to.col > 7 || from.row > 7 || to.col < 0 || from.row < 0) return
        val piece = pieceAt(from) ?: return

        pieceAt(to)?.let {
            if (it.player == piece.player) return
            piecesBox.remove(it)
        }

        piecesBox.remove(piece)
        addPiece(piece.copy(col = to.col, row = to.row))
    }

    fun addPiece(piece: ChessPiece) {
        piecesBox.add(piece)
    }

    fun clear() {
        piecesBox.clear()
    }

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            pieceAt(Square(nextCol, from.row))?.let {
                return false
            }
        }
        return true
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            pieceAt(Square(from.col, nextRow))?.let {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonallyBetween(from: Square, to: Square): Boolean {
        val gapCol = abs(from.col - to.col) - 1
        val gapRow = abs(from.row - to.row) - 1
        if (gapCol == 0 && gapRow == 0) return true
        for (i in 1..gapRow) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            pieceAt(Square(nextCol, nextRow))?.let {
                return false
            }
        }
        return true
    }

    private fun canKnightMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canRookMove(from: Square, to: Square): Boolean {
        return from.col == to.col && isClearVerticallyBetween(from, to)
                || from.row == to.row && isClearHorizontallyBetween(from, to)
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == abs(from.row - to.row)
                && isClearDiagonallyBetween(from, to)
    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canBishopMove(from, to) || canRookMove(from, to)
    }

    private fun canKingMove(from: Square, to: Square): Boolean {
        return 1 >= abs(from.col - to.col) && 1 >= abs(from.row - to.row)
    }

    fun canMove(from: Square, to: Square): Boolean {
        if (from.row == to.row && from.col == to.col) return false
        val piece = pieceAt(from) ?: return false
        return when (piece.chessman) {
            Chessman.KING -> canKingMove(from, to)
            Chessman.QUEEN -> canQueenMove(from, to)
            Chessman.ROOK -> canRookMove(from, to)
            Chessman.BISHOP -> canBishopMove(from, to)
            Chessman.KNIGHT -> canKnightMove(from, to)
            Chessman.PAWN -> true
        }
    }

    fun reset() {
        clear()
        for (col in 0..7) {
            addPiece(
                ChessPiece(col, 1, Player.WHITE, Chessman.PAWN, R.drawable.chess_plt)
            )
            addPiece(
                ChessPiece(col, 6, Player.BLACK, Chessman.PAWN, R.drawable.chess_pdt)
            )
        }
        for (i in 0..1) {
            addPiece(
                ChessPiece(i * 7, 0, Player.WHITE, Chessman.ROOK, R.drawable.chess_rlt)
            )
            addPiece(
                ChessPiece(i * 7, 7, Player.BLACK, Chessman.ROOK, R.drawable.chess_rdt)
            )
            addPiece(
                ChessPiece(1 + i * 5, 0, Player.WHITE, Chessman.KNIGHT, R.drawable.chess_nlt)
            )
            addPiece(
                ChessPiece(1 + i * 5, 7, Player.BLACK, Chessman.KNIGHT, R.drawable.chess_ndt)
            )
            addPiece(
                ChessPiece(2 + i * 3, 0, Player.WHITE, Chessman.BISHOP, R.drawable.chess_blt)
            )
            addPiece(
                ChessPiece(2 + i * 3, 7, Player.BLACK, Chessman.BISHOP, R.drawable.chess_bdt)
            )
        }
        addPiece(
            ChessPiece(3, 0, Player.WHITE, Chessman.QUEEN, R.drawable.chess_qlt)
        )
        addPiece(
            ChessPiece(3, 7, Player.BLACK, Chessman.QUEEN, R.drawable.chess_qdt)
        )
        addPiece(
            ChessPiece(4, 0, Player.WHITE, Chessman.KING, R.drawable.chess_klt)
        )
        addPiece(
            ChessPiece(4, 7, Player.BLACK, Chessman.KING, R.drawable.chess_kdt)
        )
    }

    override fun pieceAt(square: Square): ChessPiece? {
        piecesBox.forEach {
            if (square.col == it.col && square.row == it.row) {
                return it
            }
        }
        return null
    }

    fun pgnBoard(): String {
        var desc = ""
        desc += "  a b c d e f g h\n"
        for (row in 7 downTo 0) {
            desc += "${row + 1}"
            desc += boardRow(row)
            desc += " ${row + 1}\n"
        }
        desc += "  a b c d e f g h"
        return desc
    }

    override fun toString(): String {
        var desc = ""
        for (row in 7 downTo 0) {
            desc += "$row"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }

    private fun boardRow(row: Int): String {
        var desc = ""
        for (col in 0..7) {
            val piece = pieceAt(Square(col, row))
            desc += " "
            desc += piece?.let {
                val white = piece.player == Player.WHITE
                when (piece.chessman) {
                    Chessman.KING -> if (white) "k" else "K"
                    Chessman.QUEEN -> if (white) "q" else "Q"
                    Chessman.ROOK -> if (white) "r" else "R"
                    Chessman.BISHOP -> if (white) "b" else "B"
                    Chessman.KNIGHT -> if (white) "n" else "N"
                    Chessman.PAWN -> if (white) "p" else "P"
                }
            } ?: "."
        }
        return desc
    }
}