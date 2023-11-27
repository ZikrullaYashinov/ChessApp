package zikrulla.production.chessapp.model

import junit.framework.TestCase
import org.junit.jupiter.api.Test
import zikrulla.production.chessapp.ChessGame

class KnightTest : TestCase() {
    @Test
    fun testCanKnightMoveSinglePiece() {
        ChessGame.clear()
        ChessGame.addPiece(ChessPiece(3, 3, Player.WHITE, Chessman.KNIGHT, -1))
        println(ChessGame)
//        assertTrue(ChessGame.canMove(Square(3, 3), Square(5, 4), moveWhite))
//        assertFalse(ChessGame.canMove(Square(3, 3), Square(4, 4), moveWhite))
    }
}