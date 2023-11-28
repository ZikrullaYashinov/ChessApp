package zikrulla.production.chessapp

import zikrulla.production.chessapp.model.ChessPiece
import zikrulla.production.chessapp.model.Square

interface ChessDelegate {
    fun pieceAt(square: Square): ChessPiece?
    fun movePiece(from: Square, to: Square, isShow: Boolean? = null): Boolean
}