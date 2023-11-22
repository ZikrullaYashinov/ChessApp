package zikrulla.production.chessapp.model

import junit.framework.TestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChessGameTest : TestCase() {

    @BeforeEach
    public override fun setUp() {
    }

    @Test
    fun testToString() {
        assertTrue(
            ChessGame.toString().contains(
                "" +
                        "7 R N B Q K B N R\n" +
                        "6 P P P P P P P P\n" +
                        "5 . . . . . . . .\n" +
                        "4 . . . . . . . .\n" +
                        "3 . . . . . . . .\n" +
                        "2 . . . . . . . .\n" +
                        "1 p p p p p p p p\n" +
                        "0 r n b q k b n r\n" +
                        "  0 1 2 3 4 5 6 7"
            )
        )
    }

    @Test
    fun testPng() {
        println(ChessGame.pgn())
    }

    @Test
    fun testMovePiece() {
        assertNull(ChessGame.pieceAt(0, 2))
        ChessGame.movePiece(0, 1, 0, 2)
        assertNotNull(ChessGame.pieceAt(0, 2))

    }

    @Test
    fun testReset() {
    }

    @Test
    fun testPieceAt() {
        assertNotNull(ChessGame.pieceAt(0, 0))
        assertEquals(ChessPlayer.WHITE, ChessGame.pieceAt(0, 0)?.player)
    }
}