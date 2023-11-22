package zikrulla.production.chessapp.model

import zikrulla.production.chessapp.R

object ChessGame : ChessDelegate {

    private var piecesBox = mutableSetOf<ChessPiece>()

    init {
        reset()
//        movePiece(0, 1, 0, 3)
//        movePiece(0, 0, 1, 0)
//        movePiece(1, 0, 0, 0)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromRow == toRow && fromCol == toCol) return
        val piece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == piece.player) return
            piecesBox.remove(it)
        }

        piecesBox.remove(piece)
        piecesBox.add(piece.copy(col = toCol, row = toRow))
    }

    fun reset() {
        piecesBox.clear()
        for (col in 0..7) {
            piecesBox.add(
                ChessPiece(col, 1, ChessPlayer.WHITE, ChessRank.PAWN, R.drawable.chess_plt)
            )
            piecesBox.add(
                ChessPiece(col, 6, ChessPlayer.BLACK, ChessRank.PAWN, R.drawable.chess_pdt)
            )
        }
        for (i in 0..1) {
            piecesBox.add(
                ChessPiece(i * 7, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.chess_rlt)
            )
            piecesBox.add(
                ChessPiece(i * 7, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.chess_rdt)
            )
            piecesBox.add(
                ChessPiece(1 + i * 5, 0, ChessPlayer.WHITE, ChessRank.KNIGHT, R.drawable.chess_nlt)
            )
            piecesBox.add(
                ChessPiece(1 + i * 5, 7, ChessPlayer.BLACK, ChessRank.KNIGHT, R.drawable.chess_ndt)
            )
            piecesBox.add(
                ChessPiece(2 + i * 3, 0, ChessPlayer.WHITE, ChessRank.BISHOP, R.drawable.chess_blt)
            )
            piecesBox.add(
                ChessPiece(2 + i * 3, 7, ChessPlayer.BLACK, ChessRank.BISHOP, R.drawable.chess_bdt)
            )
        }
        piecesBox.add(
            ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.QUEEN, R.drawable.chess_qlt)
        )
        piecesBox.add(
            ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.QUEEN, R.drawable.chess_qdt)
        )
        piecesBox.add(
            ChessPiece(4, 0, ChessPlayer.WHITE, ChessRank.KING, R.drawable.chess_klt)
        )
        piecesBox.add(
            ChessPiece(4, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.chess_kdt)
        )
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        piecesBox.forEach {
            if (col == it.col && row == it.row) {
                return it
            }
        }
        return null
    }

    fun pgn(): String {
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
            val piece = pieceAt(col, row)
            desc += " "
            desc += piece?.let {
                val white = piece.player == ChessPlayer.WHITE
                when (piece.rank) {
                    ChessRank.KING -> if (white) "k" else "K"
                    ChessRank.QUEEN -> if (white) "q" else "Q"
                    ChessRank.ROOK -> if (white) "r" else "R"
                    ChessRank.BISHOP -> if (white) "b" else "B"
                    ChessRank.KNIGHT -> if (white) "n" else "N"
                    ChessRank.PAWN -> if (white) "p" else "P"
                }
            } ?: "."
        }
        return desc
    }
}