package zikrulla.production.chessapp.model

import junit.framework.TestCase
import org.junit.jupiter.api.Test

class RookTest : TestCase() {

    @Test
    fun testRookMoveHorizontally() {
        ChessGame.clear()
        ChessGame.addPiece(ChessPiece(3, 3, Player.WHITE, Chessman.ROOK, -1))
        ChessGame.addPiece(ChessPiece(5, 3, Player.WHITE, Chessman.KNIGHT, -1))
        println(ChessGame)
        assertFalse(ChessGame.canMove(Square(3, 3), Square(7, 3)))
    }

    @Test
    fun testRookMoveVertically() {
        ChessGame.clear()
        ChessGame.addPiece(ChessPiece(3, 3, Player.WHITE, Chessman.ROOK, -1))
        ChessGame.addPiece(ChessPiece(3, 5, Player.WHITE, Chessman.KNIGHT, -1))
        println(ChessGame)
        assertFalse(ChessGame.canMove(Square(3, 3), Square(3, 6)))
    }
}