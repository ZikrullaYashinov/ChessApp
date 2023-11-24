package zikrulla.production.chessapp.model

import junit.framework.TestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChessGameTest : TestCase() {

    @BeforeEach
    public override fun setUp() {
    }

    @Test
    fun testClear() {
        assertNotNull(ChessGame.pieceAt(Square(0, 0)))
        ChessGame.clear()
        assertNull(ChessGame.pieceAt(Square(0, 0)))
    }

    @Test
    fun testToString() {
        assertTrue(
            ChessGame.toString().contains(
                """
                    7 R N B Q K B N R
                    6 P P P P P P P P
                    5 . . . . . . . .
                    4 . . . . . . . .
                    3 . . . . . . . .
                    2 . . . . . . . .
                    1 p p p p p p p p
                    0 r n b q k b n r
                      0 1 2 3 4 5 6 7
                """.trimIndent()
            )
        )
    }

    @Test
    fun testPng() {
        println(ChessGame.toString())
    }

    @Test
    fun testMovePiece() {
//        assertNull(ChessGame.pieceAt(0, 2))
//        ChessGame.movePiece(, 2)
//        assertNotNull(ChessGame.pieceAt(0, 2))

    }

    @Test
    fun testReset() {
    }

    @Test
    fun testPieceAt() {
//        assertNotNull(ChessGame.pieceAt(0, 0))
//        assertEquals(ChessPlayer.WHITE, ChessGame.pieceAt(0, 0)?.player)
    }
}