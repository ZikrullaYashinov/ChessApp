package zikrulla.production.chessapp.model

import junit.framework.TestCase
import org.junit.jupiter.api.Test

class KnightTest : TestCase() {
    @Test
    fun testCanKnightMoveSinglePiece() {
        ChessGame.clear()
        ChessGame.addPiece(ChessPiece(3, 3, Player.WHITE, Chessman.KNIGHT, -1))
        println(ChessGame)
        assertTrue(ChessGame.canMove(Square(3, 3), Square(5, 4)))
        assertFalse(ChessGame.canMove(Square(3, 3), Square(4, 4)))
    }
}