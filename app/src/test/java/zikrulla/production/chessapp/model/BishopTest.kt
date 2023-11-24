package zikrulla.production.chessapp.model

import junit.framework.TestCase
import org.junit.jupiter.api.Test

class BishopTest : TestCase() {
    @Test
    fun testBishopMove() {
        ChessGame.clear()
        ChessGame.addPiece(ChessPiece(3, 3, Player.WHITE, Chessman.BISHOP, -1))
        ChessGame.addPiece(ChessPiece(4, 4, Player.WHITE, Chessman.KNIGHT, -1))
        println(ChessGame)
        assertFalse(ChessGame.canMove(Square(3, 3), Square(7, 7)))
//        assertFalse(ChessGame.canMove(Square(3, 3), Square(5, 4)))
    }

    @Test
    fun testBishopDiagonally() {
    }
}