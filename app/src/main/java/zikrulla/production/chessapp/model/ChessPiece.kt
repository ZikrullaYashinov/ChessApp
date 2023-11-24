package zikrulla.production.chessapp.model

data class ChessPiece(
    val col: Int,
    val row: Int,
    val player: Player,
    val chessman: Chessman,
    val resId: Int
)
